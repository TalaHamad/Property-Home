package Classes;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.Parent;

import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

import java.util.Objects;
import java.io.IOException;

public class Main extends Application
{
    public static void main(String []args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        try
        {
            Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML_Files/Splash.fxml")));
            Scene scene=new Scene(root);
            Image icon = new Image("/Images/icon.jfif");
            stage.setTitle("Property Home");
            stage.getIcons().add(icon);
            stage.setWidth(600);
            stage.setHeight(400);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
    }
}