package Controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.event.Event;

import java.time.LocalDate;
import java.io.IOException;

public class SearchAppointmentDialogController
{
    static Stage stage;
    static String screenSource;

    @FXML
    private DatePicker date;
    @FXML
    private Button searchButton;
    @FXML
    private Button cancelButton;

    @FXML
    void searchEvent()
    {
        if(this.date.getValue()==null)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Can't Search An Appointment");
            alert.setContentText("Please Choose Appointment Date");
            alert.show();
        }
        else
        {
            try
            {
                LocalDate localDate=date.getValue();
                String appDate=""+localDate.getYear()+""+"-"+""+localDate.getMonthValue()+""+"-"+""+localDate.getDayOfMonth()+"";
                String query1="select *from appointments where appointment_date=TO_DATE('"+appDate+"','YYYY-MM-DD')";
                String query2="select *from appointments where appointment_date=TO_DATE('"+appDate+"','YYYY-MM-DD') and u_id="+HomeScreenController.user.getUserID()+"";

                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root=null;

                if(screenSource.equals("Admin"))
                {
                    fxmlLoader.setLocation(getClass().getResource("/FXML_Files/AdminAppointmentsScreen.fxml"));
                    root = fxmlLoader.load();
                    AdminAppointmentsScreenController adminAppointmentsScreenController=fxmlLoader.getController();
                    adminAppointmentsScreenController.fillTableData(query1);
                    adminAppointmentsScreenController.changeSearchButtonToCancelButton("Cancel");
                }
                else if(screenSource.equals("User"))
                {
                    fxmlLoader.setLocation(getClass().getResource("/FXML_Files/MyAppointmentsScreen.fxml"));
                    root = fxmlLoader.load();
                    MyAppointmentsScreenController myAppointmentsScreenController = fxmlLoader.getController();
                    myAppointmentsScreenController.fillTableData(query2);
                    myAppointmentsScreenController.changeSearchButtonToCancelButton("Cancel");
                }

                Scene scene=new Scene(root);
                SplashController.mainStage.setScene(scene);
                stage.close();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText("Error Occurred While Loading Appointments Screen.");
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