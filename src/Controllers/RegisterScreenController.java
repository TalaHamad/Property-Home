package Controllers;

import javafx.scene.Scene;
import javafx.scene.Parent;
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

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;
import java.io.IOException;

import Models.User;

public class RegisterScreenController
{
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField ssnField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    @FXML
    private ToggleGroup gender;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backButton;

    @FXML
    public void signUpEvent()
    {
        int ssn=-1,userID;
        long phoneNumber;
        String firstName,lastName,address,email,dateOfBirth,password,gender,type,recoveryCode;
        LocalDate localDate;
        if(this.checkIfDataIsValid())
        {
            firstName=this.firstNameField.getText();
            lastName=this.lastNameField.getText();
            ssn=Integer.parseInt(this.ssnField.getText());
            address=this.addressField.getText();
            phoneNumber=Long.parseLong(this.phoneNumberField.getText());
            email=this.emailField.getText();
            password=this.passwordField.getText();
            localDate=this.dateOfBirthField.getValue();
            dateOfBirth=""+localDate.getYear()+""+"-"+""+localDate.getMonthValue()+""+"-"+""+localDate.getDayOfMonth()+"";
            gender=(this.gender.getSelectedToggle()==this.male)?"Male":"Female";
            type=(password.equals("Admin@2021"))?"Admin":"User";
            recoveryCode=this.generateRandomRecoveryCode();

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
                String query="insert into person values('"+firstName+"','"+lastName+"',"+ssn+",TO_DATE('"+dateOfBirth+"','YYYY-MM-DD'),'"+gender+"','"+address+"',"+phoneNumber+")";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="insert into users VALUES(UserId_seq.NextVal,"+ssn+",'"+email+"','"+password+"','"+type+"','"+recoveryCode+"')";
                ps=con.prepareStatement(query);
                ps.executeQuery();

                query="select user_id from users where user_ssn="+ssn+"";
                ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();
                resultSet.next();
                userID=resultSet.getInt(1);
                con.close();

                HomeScreenController.user=new User(firstName, lastName, ssn, localDate, gender, address, phoneNumber, userID, email, password, type);
                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(this.parentContainer.getScene().getWindow());
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Property-Home");
                alert.setContentText("Your Recovery Code Is: "+recoveryCode+", Don't Share It With Anyone.");
                alert.showAndWait();
                this.clearFields();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML_Files/HomeScreen.fxml")));
                Scene scene = this.signUpButton.getScene();
                root.translateYProperty().set(scene.getHeight());
                this.parentContainer = (AnchorPane) this.signUpButton.getScene().getRoot();
                this.parentContainer.getChildren().add(root);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(this::handle);
                timeline.play();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                this.reverseOnError(ssn);
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("This User Already Exist!");
                alert.setContentText("Maybe you've entered a pre-registered SSN, Email, Or Phone-Number.");
                alert.showAndWait();
                this.clearFields();
            }
            catch (SQLException sqlException)
            {
                this.reverseOnError(ssn);
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
    public void backToStartScreen()
    {
        this.switchScreen("/FXML_Files/StartScreen.fxml","Start");
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.signUpButton)
        {
            this.signUpButton.setCursor(Cursor.HAND);
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

        if(this.firstNameField.getText().isBlank())
        {
            this.firstNameField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*First Name Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.firstNameField.setStyle("");
        }

        if(this.lastNameField.getText().isBlank())
        {
            this.lastNameField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Last Name Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.lastNameField.setStyle("");
        }

        if(this.ssnField.getText().isBlank())
        {
            this.ssnField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*SSN Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.ssnField.getText()))
        {
            this.ssnField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*SSN IS Invalid.\n";
            error=true;
        }
        else if(this.ssnField.getText().length()>9)
        {
            this.ssnField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*SSN Must Be At Maximum 9-Digits.\n";
            error=true;
        }
        else
        {
            this.ssnField.setStyle("");
        }

        if(this.addressField.getText().isBlank())
        {
            this.addressField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Address Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.addressField.setStyle("");
        }

        if(this.phoneNumberField.getText().isBlank())
        {
            this.phoneNumberField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Phone Number Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.phoneNumberField.getText()))
        {
            this.phoneNumberField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Phone Number Is Invalid.\n";
            error=true;
        }
        else
        {
            this.phoneNumberField.setStyle("");
        }

        if(this.emailField.getText().isBlank())
        {
            this.emailField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Field Is Empty.\n";
            error=true;
        }
        else if(!this.isValidEmail(this.emailField.getText()))
        {
            this.emailField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Address Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.emailField.setStyle("");
        }

        if(this.passwordField.getText().isBlank())
        {
            this.passwordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Password Field Is Empty.\n";
            error=true;
        }
        else if(!this.isValidPassword(this.passwordField.getText()))
        {
            this.passwordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Password Is Not Valid:\n" +
                    " -your Password should not contain any space.\n" +
                    " -Password should contain at least one digit(0-9).\n" +
                    " -Password length should be between 8 to 18 characters.\n" +
                    " -Password should contain at least one lowercase letter(a-z).\n" +
                    " -Password should contain at least one uppercase letter(A-Z).\n" +
                    " -Password should contain at least one special character.\n";
            error=true;
        }
        else
        {
            this.passwordField.setStyle("");
        }

        if(this.confirmPasswordField.getText().isBlank())
        {
            this.confirmPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Confirm Password Field Is Empty.\n";
            error=true;
        }
        else if(!this.confirmPasswordField.getText().equals(this.passwordField.getText()))
        {
            this.confirmPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Passwords Didn't Match.\n";
            error=true;
        }
        else
        {
            this.confirmPasswordField.setStyle("");
        }

        if(this.dateOfBirthField.getValue()==null)
        {
            this.dateOfBirthField.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Date Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.dateOfBirthField.setStyle("");
        }

        if(this.gender.getSelectedToggle()!=this.male&&this.gender.getSelectedToggle()!=this.female)
        {
            errorMessage+="*Please Specify Your Gender.\n";
            error=true;
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

    private boolean isNumeric(String string)
    {
        for(char c:string.toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
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

    private boolean isValidPassword(String password)
    {
        if (!((password.length() >= 8) && (password.length() <= 18)))
        {
            return false;
        }

        if (password.contains(" "))
        {
            return false;
        }

        if (true)
        {
            int count = 0;

            for (int i = 0; i <= 9; i++)
            {
                String str1 = Integer.toString(i);

                if (password.contains(str1))
                {
                    count = 1;
                }
            }
            if (count == 0)
            {
                return false;
            }
        }

        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|")))
        {
            return false;
        }

        if (true)
        {
            int count = 0;

            for (int i = 65; i <= 90; i++)
            {
                char c = (char)i;

                String str1 = Character.toString(c);
                if (password.contains(str1))
                {
                    count = 1;
                }
            }
            if (count == 0)
            {
                return false;
            }
        }

        if (true)
        {
            int count = 0;

            for (int i = 90; i <= 122; i++)
            {
                char c = (char)i;
                String str1 = Character.toString(c);

                if (password.contains(str1))
                {
                    count = 1;
                }
            }

            if (count == 0)
            {
                return false;
            }
        }
        return true;
    }

    private String generateRandomRecoveryCode()
    {
        Random random = new Random();
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk" + "lmnopqrstuvwxyz!@#$%&";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++)
        {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void reverseOnError(int ssn)
    {
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");

            String query="delete from users where user_ssn="+ssn+"";
            PreparedStatement ps=con.prepareStatement(query);
            ps.executeQuery();

            query="delete from person where ssn="+ssn+"";
            ps=con.prepareStatement(query);
            ps.executeQuery();

            con.close();
        }
        catch (SQLException sqlException)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Data-Base Connection Error!");
            alert.setContentText("An Error Occurred While Connection To Data-Base.");
            alert.showAndWait();
        }
    }

    private void clearFields()
    {
        this.firstNameField.clear();
        this.lastNameField.clear();
        this.ssnField.clear();
        this.addressField.clear();
        this.phoneNumberField.clear();
        this.emailField.clear();
        this.passwordField.clear();
        this.confirmPasswordField.clear();
        this.dateOfBirthField.setValue(null);
        this.male.setSelected(false);
        this.female.setSelected(false);
    }

    private void switchScreen(String screen,String screenName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(screen)));
            Scene scene = this.signUpButton.getScene();
            root.translateXProperty().set(scene.getWidth());
            this.parentContainer = (AnchorPane) this.signUpButton.getScene().getRoot();
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
        parentContainer.getChildren().remove(this.parentContainer);
    }
}