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

import javafx.animation.*;

import javafx.event.Event;
import javafx.event.ActionEvent;

import javafx.util.Duration;
import java.util.Objects;
import java.io.IOException;

public class StartScreenController implements Initializable
{
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button logInButton;
    @FXML
    private Button signUpButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.parentContainer.setOpacity(0);
        this.fadeTransition();
    }

    @FXML
    public void switchToLoginScreen()
    {
        this.switchScreen("/FXML_Files/LogInScreen.fxml","Log In");
    }

    @FXML
    public void switchToRegisterScreen()
    {
        this.switchScreen("/FXML_Files/RegisterScreen.fxml","Register");
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.logInButton)
        {
            this.logInButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.signUpButton)
        {
            this.signUpButton.setCursor(Cursor.HAND);
        }
    }

    private void fadeTransition()
    {
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setNode(this.parentContainer);
        fadeTransition.setDuration(Duration.millis(250));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
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
        this.parentContainer.getChildren().remove(this.parentContainer);
    }
}