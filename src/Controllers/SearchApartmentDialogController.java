package Controllers;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.event.Event;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;

import java.io.IOException;

public class SearchApartmentDialogController
{
    static Stage stage;

    @FXML
    private Button searchButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField cityField;

    @FXML
    void searchEvent()
    {
        if(this.cityField.getText().isBlank())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Can't Search An Apartment");
            alert.setContentText("Please Enter Apartment City");
            alert.show();
        }
        else
        {
            try
            {
                String cityName=this.cityField.getText();
                String query="select *from apartments where city='"+cityName+"'";

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FXML_Files/AdminApartmentsScreen.fxml"));
                Parent root = fxmlLoader.load();
                AdminApartmentsScreenController adminApartmentsScreenController = fxmlLoader.getController();
                adminApartmentsScreenController.fillTableData(query);
                adminApartmentsScreenController.changeSearchButtonToCancelButton("Cancel");

                Scene scene=new Scene(root);
                SplashController.mainStage.setScene(scene);
                stage.close();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText("Error Occurred While Loading Admin Apartments Screen.");
                alert.show();
            }
        }
    }

    @FXML
    void cancelEvent()
    {
        stage.close();
    }

    @FXML
    void changeCursor(Event event)
    {
        if(event.getSource()==this.searchButton)
        {
            this.searchButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.cancelButton)
        {
            this.cancelButton.setCursor(Cursor.HAND);
        }
    }
}