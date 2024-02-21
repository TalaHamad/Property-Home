package Controllers;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.*;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import oracle.jdbc.driver.OracleDriver;

import javafx.event.Event;
import javafx.event.ActionEvent;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;
import java.io.IOException;

public class ProfileScreenController implements Initializable
{
    @FXML
    AnchorPane parentContainer;
    @FXML
    private Tooltip homeTooltip;
    @FXML
    private Button homeButton;
    @FXML
    private CheckBox changePasswordBox;
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
    private DatePicker dateOfBirthField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button myApponitmentsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.setItemsProperties();
        this.setFieldsData();
    }

    @FXML
    public void backToHomeScreen()
    {
        this.switchScreen("/FXML_Files/HomeScreen.fxml", "Home");
    }

    @FXML
    public void switchToBuyerAppointmentsScreen()
    {
        this.switchScreen("/FXML_Files/MyAppointmentsScreen.fxml", "Buyer Appointments");
    }

    @FXML
    public void updateEvent()
    {
        long phoneNumber;
        String firstName,lastName,address,email,dateOfBirth,password,gender;
        LocalDate localDate;

        if(this.checkIfDataIsValid())
        {
            firstName=this.firstNameField.getText();
            lastName=this.lastNameField.getText();
            address=this.addressField.getText();
            phoneNumber=Long.parseLong(this.phoneNumberField.getText());
            email=this.emailField.getText();
            password=this.newPasswordField.getText();
            localDate=this.dateOfBirthField.getValue();
            dateOfBirth=""+localDate.getYear()+""+"-"+""+localDate.getMonthValue()+""+"-"+""+localDate.getDayOfMonth()+"";
            gender=this.genderComboBox.getValue();

            String query1="update users set email='"+email+"' where user_id="+HomeScreenController.user.getUserID()+"";
            String query2="update users set email='"+email+"',password='"+password+"' where user_id="+HomeScreenController.user.getUserID()+"";
            String query3="update person set first_name='"+firstName+"',last_Name='"+lastName+"',address='"+address+"',phone_number="+phoneNumber+",gender='"+gender+"',date_of_birth=TO_DATE('"+dateOfBirth+"','YYYY-MM-DD') where ssn="+HomeScreenController.user.getSsn()+"";

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
                PreparedStatement ps;

                if(!changePasswordBox.isSelected())
                {
                    ps=con.prepareStatement(query1);
                }
                else
                {
                    ps=con.prepareStatement(query2);
                }

                ps.executeQuery();
                ps=con.prepareStatement(query3);
                ps.executeQuery();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(SplashController.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Property-Home");
                alert.setHeaderText(null);
                alert.setContentText("Your Information Updated Successfully.");
                alert.showAndWait();

                this.setSharedUserObject(firstName,lastName,address,email,password,phoneNumber,localDate,gender);
                this.reset();
                con.close();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("This User Already Exist!");
                alert.setContentText("Maybe you've entered a pre-registered Email, Or Phone-Number.");
                alert.showAndWait();
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
    }

    @FXML
    public void cancelEvent()
    {
        this.setFieldsData();
        this.reset();
    }

    @FXML
    void changePasswordBoxEvent()
    {
        if(this.changePasswordBox.isSelected())
        {
            this.currentPasswordField.setEditable(true);
            this.newPasswordField.setEditable(true);
            this.confirmNewPasswordField.setEditable(true);
        }
        else
        {
            this.currentPasswordField.clear();
            this.newPasswordField.clear();
            this.confirmNewPasswordField.clear();

            this.currentPasswordField.setEditable(false);
            this.newPasswordField.setEditable(false);
            this.confirmNewPasswordField.setEditable(false);
        }
    }

    @FXML
    public void changeCursor(Event event)
    {
        if (event.getSource() == this.homeButton)
        {
            this.homeButton.setCursor(Cursor.HAND);
        }
        else if (event.getSource() == this.myApponitmentsButton)
        {
            this.myApponitmentsButton.setCursor(Cursor.HAND);
        }
        else if (event.getSource() == this.updateButton)
        {
            this.updateButton.setCursor(Cursor.HAND);
        }
        else if (event.getSource() == this.cancelButton)
        {
            this.cancelButton.setCursor(Cursor.HAND);
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

        if(this.changePasswordBox.isSelected())
        {
            if(this.currentPasswordField.getText().isBlank())
            {
                this.currentPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Current Password Field Is Empty.\n";
                error=true;
            }
            else if(!this.currentPasswordField.getText().equals(HomeScreenController.user.getPassword()))
            {
                this.currentPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Current Password Is Not True.\n";
                error=true;
            }
            else
            {
                this.currentPasswordField.setStyle("");
            }

            if(this.newPasswordField.getText().isBlank())
            {
                this.newPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*New Password Field Is Empty.\n";
                error=true;
            }

            else if(!this.isValidPassword(this.newPasswordField.getText()))
            {
                this.newPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*New Password Is Not Valid:\n" +
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
                this.newPasswordField.setStyle("");
            }

            if(this.confirmNewPasswordField.getText().isBlank())
            {
                this.confirmNewPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Confirm Password Field Is Empty.\n";
                error=true;
            }
            else if(!this.confirmNewPasswordField.getText().equals(this.newPasswordField.getText()))
            {
                this.confirmNewPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Passwords Didn't Match.\n";
                error=true;
            }
            else
            {
                this.confirmNewPasswordField.setStyle("");
            }
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
        for (char c : string.toCharArray())
        {
            if (!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

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

    private void setFieldsData()
    {
        this.firstNameField.setText(HomeScreenController.user.getFirstName());
        this.lastNameField.setText(HomeScreenController.user.getLastName());
        this.ssnField.setText(Integer.toString(HomeScreenController.user.getSsn()));
        this.addressField.setText(HomeScreenController.user.getAddress());
        this.phoneNumberField.setText(Long.toString(HomeScreenController.user.getPhoneNumber()));
        this.emailField.setText(HomeScreenController.user.getEmail());
        this.dateOfBirthField.setValue(HomeScreenController.user.getDob());
        if (HomeScreenController.user.getGender().equalsIgnoreCase("Male"))
        {
            this.genderComboBox.getSelectionModel().select("Male");
        }
        else if (HomeScreenController.user.getGender().equalsIgnoreCase("Female"))
        {
            this.genderComboBox.getSelectionModel().select("Female");
        }
    }

    private void setSharedUserObject(String firstName,String lastName,String address,String email,String password,long phoneNumber,LocalDate dateOfBirth,String gender)
    {
        HomeScreenController.user.setFirstName(firstName);
        HomeScreenController.user.setLastName(lastName);
        HomeScreenController.user.setAddress(address);
        HomeScreenController.user.setPhoneNumber(phoneNumber);
        HomeScreenController.user.setEmail(email);
        HomeScreenController.user.setPassword(password);
        HomeScreenController.user.setDob(dateOfBirth);
        HomeScreenController.user.setGender(gender);
    }

    private void setItemsProperties()
    {
        this.homeTooltip.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-pref-width: 50; -fx-pref-height: 30;");
        this.genderComboBox.getItems().add("Male");
        this.genderComboBox.getItems().add("Female");
    }

    private void reset()
    {
        this.changePasswordBox.setSelected(false);
        this.currentPasswordField.clear();
        this.newPasswordField.clear();
        this.confirmNewPasswordField.clear();
        this.currentPasswordField.setEditable(false);
        this.newPasswordField.setEditable(false);
        this.confirmNewPasswordField.setEditable(false);

        this.confirmNewPasswordField.setStyle("");
        this.newPasswordField.setStyle("");
        this.currentPasswordField.setStyle("");
        this.emailField.setStyle("");
        this.phoneNumberField.setStyle("");
        this.addressField.setStyle("");
        this.lastNameField.setStyle("");
        this.firstNameField.setStyle("");
    }

    private void switchScreen(String screen, String screenName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(screen)));
            Scene scene = this.updateButton.getScene();
            root.translateXProperty().set(scene.getWidth());
            this.parentContainer = (AnchorPane) this.updateButton.getScene().getRoot();
            this.parentContainer.getChildren().add(root);
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(this::handle);
            timeline.play();
        }
        catch (NullPointerException | IOException exception)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Loading Screen Error!");
            alert.setContentText("Error Occurred While Loading " + screenName + " Screen.");
            alert.show();
        }
    }

    private void handle(ActionEvent t)
    {
        this.parentContainer.getChildren().remove(this.parentContainer);
    }
}