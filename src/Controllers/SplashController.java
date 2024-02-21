package Controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;

import javafx.scene.image.Image;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import java.util.Objects;
import java.io.IOException;

public class SplashController implements Initializable
{
    static Stage mainStage;
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.splash();
    }

    private void splash()
    {
        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call() throws Exception
            {
                int steps = 200;
                for (int i = 0; i < steps; i++)
                {
                    Thread.sleep(10);
                    updateProgress(i, steps);
                    updateMessage(String.valueOf(i));
                }
                return null;
            }
        };
        task.setOnFailed(wse -> wse.getSource().getException().printStackTrace());
        task.setOnSucceeded(wse ->
        {
            try
            {
                mainStage=new Stage();
                Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML_Files/AboutUsScreen.fxml")));
                Scene loginScene=new Scene(root);
                Image icon = new Image("/Images/icon.jfif");
                mainStage.setTitle("Property Home");
                mainStage.getIcons().add(icon);
                mainStage.setWidth(815);
                mainStage.setHeight(840);
                mainStage.setResizable(false);
                mainStage.setScene(loginScene);
                mainStage.show();
                this.parentContainer.getScene().getWindow().hide();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(this.parentContainer.getScene().getWindow());
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText("Error Occurred While Loading About Us Screen.");
                alert.show();
            }
        });
        this.progressBar.progressProperty().bind(task.progressProperty());
        this.progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
}