package Controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.event.Event;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.io.IOException;

public class AboutUsScreenController
{
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button getStartedButton;

    @FXML
    private void switchToStartScreen()
    {
        this.fadeTransition();
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.getStartedButton)
        {
            this.getStartedButton.setCursor(Cursor.HAND);
        }
    }

    private void fadeTransition()
    {
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setNode(this.parentContainer);
        fadeTransition.setDuration(Duration.millis(250));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished((ActionEvent)->
        {
            this.loadNextScreen();
        });
        fadeTransition.play();
    }

    private void loadNextScreen()
    {
        try
        {
            Parent root=FXMLLoader.load(getClass().getResource("/FXML_Files/StartScreen.fxml"));
            Scene scene=new Scene(root);
            SplashController.mainStage.setScene(scene);
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
}