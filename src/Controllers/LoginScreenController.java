package Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.*;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import oracle.jdbc.driver.OracleDriver;

import javafx.event.Event;
import javafx.event.ActionEvent;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.io.IOException;

import Models.User;

public class LoginScreenController implements Initializable
{
    private SimpleBooleanProperty showPassword ;
    private  Label message;
    private Label label;

    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button backButton;
    @FXML
    private TextField emailAddressField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button passwordResetButton;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Tooltip toolTip;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.showPassword = new SimpleBooleanProperty();
        this.toolTip = new Tooltip();
        this.message = new Label("");
        this.label = new Label("Password");

        this.toolTip.setShowDelay(Duration.ZERO);
        this.toolTip.setAutoHide(false);
        this.toolTip.setMinWidth(50);

        this.showPassword.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) ->
        {
            if(newValue)
            {
                showPassword();
            }
            else
            {
                hidePassword();
            }
        });

        this.passwordField.setOnKeyTyped(e ->
        {
            if ( showPassword.get() )
            {
                showPassword();
            }
        });

        this.showPassword.bind(this.checkBox.selectedProperty());
    }

    @FXML
    public void logInEvent()
    {
        int ssn,userID;
        long phoneNumber;
        String firstName,lastName,email,password,dob,address,gender,type;
        LocalDate dateOfBirth;

        if(this.checkIfDataIsValid())
        {
            email=this.emailAddressField.getText();
            password=this.passwordField.getText();

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
                String query="select *from users where email='"+email+"'and password='"+password+"'";
                PreparedStatement ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();

                if(resultSet.next())
                {
                    userID=resultSet.getInt(1);
                    ssn=resultSet.getInt(2);
                    type=resultSet.getString(5);
                    query="select *from person where ssn="+ssn+"";
                    ps=con.prepareStatement(query);
                    resultSet=ps.executeQuery();
                    resultSet.next();
                    firstName=resultSet.getString(1);
                    lastName=resultSet.getString(2);
                    dob=resultSet.getString(4);
                    dateOfBirth=LocalDate.of(Integer.parseInt(dob.split(" ")[0].split("-")[0]),Integer.parseInt(dob.split(" ")[0].split("-")[1]),Integer.parseInt(dob.split(" ")[0].split("-")[2]));
                    gender=resultSet.getString(5);
                    address=resultSet.getString(6);
                    phoneNumber=resultSet.getLong(7);

                    HomeScreenController.user=new User(firstName, lastName, ssn, dateOfBirth, gender, address, phoneNumber, userID, email, password, type);
                    this.checkBox.setSelected(false);
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML_Files/HomeScreen.fxml")));
                    Scene scene = this.loginButton.getScene();
                    root.translateYProperty().set(scene.getHeight());
                    this.parentContainer = (AnchorPane) this.loginButton.getScene().getRoot();
                    this.parentContainer.getChildren().add(root);
                    Timeline timeline = new Timeline();
                    KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                    KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
                    timeline.getKeyFrames().add(kf);
                    timeline.setOnFinished(this::handle);
                    timeline.play();
                }
                else
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(SplashController.mainStage);
                    alert.setHeaderText("No User Found!");
                    alert.setContentText("Maybe you've entered a wrong Email or Password.");
                    alert.showAndWait();
                }
                con.close();
                this.clearFields();
            }
            catch (SQLException sqlException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText("An Error Occurred While Connection To Data-Base.");
                alert.showAndWait();
            }
            catch(IOException | NullPointerException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText("Error Occurred While Loading Home Screen.");
                alert.show();
            }
        }
    }

    @FXML
    public void forgotPasswordEvent()
    {
        TextInputDialog dialog1 = new TextInputDialog();
        dialog1.initOwner(SplashController.mainStage);
        dialog1.setTitle("Password Recovery");
        dialog1.setHeaderText(null);
        dialog1.setContentText("Please Enter Your Email: ");
        Optional<String> result1 = dialog1.showAndWait();

        if (result1.isPresent())
        {
            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
                String query="select *from users where email='"+result1.get()+"'";
                PreparedStatement ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();

                if(resultSet.next())
                {
                    TextInputDialog dialog2 = new TextInputDialog();
                    dialog2.initOwner(SplashController.mainStage);
                    dialog2.setHeaderText(null);
                    dialog2.setTitle("Password Recovery");
                    dialog2.setContentText("Please Enter Your Recovery Code:");
                    Optional<String> result2 = dialog2.showAndWait();

                    if(result2.isPresent())
                    {
                        query="select password from users where recovery_code='"+result2.get()+"'";
                        ps=con.prepareStatement(query);
                        resultSet=ps.executeQuery();

                        if(resultSet.next())
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.initOwner(SplashController.mainStage);
                            alert.setTitle("Password Recovery");
                            alert.setHeaderText(null);
                            alert.setContentText("Your Password Is: "+resultSet.getString(1)+"");
                            alert.showAndWait();
                        }
                        else
                        {
                            Alert alert=new Alert(Alert.AlertType.ERROR);
                            alert.initOwner(SplashController.mainStage);
                            alert.setHeaderText("No Recovery Code Found!");
                            alert.setContentText("Please Make Sure That You Entered Your Given Recovery Code.");
                            alert.showAndWait();
                        }
                    }
                    else
                    {
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(SplashController.mainStage);
                        alert.setHeaderText("No Recovery Code Entered!");
                        alert.setContentText("Please Enter Your Recovery Code.");
                        alert.showAndWait();
                    }
                }
                else
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(SplashController.mainStage);
                    alert.setHeaderText("No Email Found!");
                    alert.setContentText("Please Make Sure That You Entered Your Registered Email.");
                    alert.showAndWait();
                }
                con.close();
            }
            catch(SQLException sqlException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText("An Error Occurred While Connection To Data-Base.");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("No Email Entered!");
            alert.setContentText("Please Enter Your Email.");
            alert.showAndWait();
        }
    }

    @FXML
    public void backToStartScreen()
    {
        this.switchScreen("/FXML_Files/StartScreen.fxml","Start");
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.loginButton)
        {
            this.loginButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.passwordResetButton)
        {
            this.passwordResetButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.backButton)
        {
            this.backButton.setCursor(Cursor.HAND);
        }
    }

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.emailAddressField.getText().isBlank())
        {
            this.emailAddressField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Address Field Is Empty.\n";
            error=true;
        }
        else if(!this.isValidEmail(this.emailAddressField.getText()))
        {
            this.emailAddressField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Address Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.emailAddressField.setStyle("");
        }

        if(this.passwordField.getText().isBlank())
        {
            this.passwordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Password Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.passwordField.setStyle("");
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.parentContainer.getScene().getWindow());
            alert.setHeaderText("Data Is Invalid!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        if (email == null)
        {
            return false;
        }

        return pat.matcher(email).matches();
    }

    private void clearFields()
    {
        this.emailAddressField.clear();
        this.passwordField.clear();
    }

    private void showPassword()
    {
        Point2D p = this.passwordField.localToScene(this.passwordField.getBoundsInLocal().getMaxX(), this.passwordField.getBoundsInLocal().getMaxY());
        toolTip.setText(this.passwordField.getText());
        toolTip.show(this.passwordField,
                p.getX() + SplashController.mainStage.getScene().getX() + SplashController.mainStage.getX(),
                p.getY() + SplashController.mainStage.getScene().getY() + SplashController.mainStage.getY());
    }

    private void hidePassword()
    {
        toolTip.setText("");
        toolTip.hide();
    }

    private void switchScreen(String screen,String screenName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(screen)));
            Scene scene = this.loginButton.getScene();
            root.translateXProperty().set(scene.getWidth());
            this.parentContainer = (AnchorPane) this.loginButton.getScene().getRoot();
            this.parentContainer.getChildren().add(root);
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(this::handle);
            timeline.play();
        }
        catch(NullPointerException | IOException exception)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Loading Screen Error!");
            alert.setContentText("Error Occurred While Loading "+screenName+" Screen.");
            alert.show();
        }
    }

    private void handle(ActionEvent t)
    {
        this.parentContainer.getChildren().remove(this.parentContainer);
    }
}