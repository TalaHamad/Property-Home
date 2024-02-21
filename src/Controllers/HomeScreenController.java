package Controllers;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.Button;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.ActionEvent;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.util.Objects;
import java.io.IOException;

import Models.User;

public class HomeScreenController implements Initializable
{
    static User user;

    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button profileButton;
    @FXML
    private Button buyButton;
    @FXML
    private Button rentButton;
    @FXML
    private Button achievementsButton;
    @FXML
    private Button aboutUsButton;
    @FXML
    private Button logoutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if(this.user!=null)
        {
            if(this.user.getType().equalsIgnoreCase("User"))
            {
                this.achievementsButton.setVisible(false);
            }
            else if(this.user.getType().equalsIgnoreCase("Admin"))
            {
                this.achievementsButton.setVisible(true);
            }
        }
    }

    @FXML
    public void switchToProfileScreen()
    {
        if(user!=null)
        {
            if(user.getType().equalsIgnoreCase("User"))
            {
                this.switchScreen("/FXML_Files/ProfileScreen.fxml","User Profile");
            }
            else if(user.getType().equalsIgnoreCase("Admin"))
            {
                this.switchScreen("/FXML_Files/AdminProfileScreen.fxml","Admin Profile");
            }
        }
    }

    @FXML
    public void switchToBuyScreen()
    {
        BuyRentScreenController.apartmentType="Sell";
        this.switchScreen("/FXML_Files/BuyRentScreen.fxml","Sell");
    }

    @FXML
    public void switchToRentScreen()
    {
        BuyRentScreenController.apartmentType="Rent";
        this.switchScreen("/FXML_Files/BuyRentScreen.fxml","Rent");
    }

    @FXML
    public void switchToAchievementsScreen()
    {
        this.switchScreen("/FXML_Files/AchievementsScreen.fxml","Achievements");
    }

    @FXML
    public void switchToAboutUsScreen()
    {
        this.switchScreen("/FXML_Files/ContactUsScreen.fxml","About Us");
    }

    @FXML
    public void logoutEvent()
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML_Files/StartScreen.fxml")));
            Scene scene = this.logoutButton.getScene();
            root.translateYProperty().set(scene.getHeight());
            this.parentContainer = (AnchorPane) this.logoutButton.getScene().getRoot();
            this.parentContainer.getChildren().add(root);
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
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
            alert.setContentText("Error Occurred While Loading Start Screen.");
            alert.show();
        }
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.profileButton)
        {
            this.profileButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.buyButton)
        {
            this.buyButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.rentButton)
        {
            this.rentButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.achievementsButton)
        {
            this.achievementsButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.aboutUsButton)
        {
            this.aboutUsButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.logoutButton)
        {
            this.logoutButton.setCursor(Cursor.HAND);
        }
    }

    private void switchScreen(String screen,String screenName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(screen)));
            Scene scene = this.profileButton.getScene();
            root.translateXProperty().set(scene.getWidth());
            this.parentContainer = (AnchorPane) this.parentContainer.getScene().getRoot();
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