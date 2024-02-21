package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import oracle.jdbc.driver.OracleDriver;

import javafx.event.Event;

import java.sql.*;
import java.util.ArrayList;
import javafx.util.Callback;
import java.time.LocalDate;

import Models.Apartment;

public class AssignAppointmentDialogController implements Initializable
{
    private Apartment apartment;

    @FXML
    private DatePicker appointmentDate;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.apartment=BuyRentScreenController.apartment;
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableBookedAndPassedDays();
        this.appointmentDate.setDayCellFactory(dayCellFactory);
    }

    @FXML
    void confirmEvent()
    {
        if(this.appointmentDate.getValue()==null)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.confirmButton.getScene().getWindow());
            alert.setHeaderText("Can't Book An Appointment");
            alert.setContentText("Please Choose Appointment Date");
            alert.show();
        }
        else
        {
            try
            {
                LocalDate localDate=this.appointmentDate.getValue();
                String appDate=""+localDate.getYear()+""+"-"+""+localDate.getMonthValue()+""+"-"+""+localDate.getDayOfMonth()+"";

                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
                String query="insert into appointments values(appointmentId_seq.NextVal,TO_DATE('"+appDate+"','YYYY-MM-DD'),"+HomeScreenController.user.getUserID()+","+this.apartment.getApartmentID()+")";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();
                query="update apartments set state='Booked' where apartment_id="+this.apartment.getApartmentID()+"";
                ps=con.prepareStatement(query);
                ps.executeQuery();
                con.close();

                this.apartment.setState("Booked");
                Parent root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML_Files/BuyRentScreen.fxml")));
                Scene buyRentScene=new Scene(root);
                SplashController.mainStage.setScene(buyRentScene);
                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText(null);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setContentText("Your Appointment Assigned Successfully.");
                alert.showAndWait();
                BuyRentScreenController.stageDialog.close();
            }
            catch(SQLException sqlException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText("An Error Occurred While Connection To Data-Base.");
                alert.showAndWait();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText("Error Occurred While Loading Buy&Rent Screen.");
                alert.show();
            }
        }
    }

    @FXML
    void cancelEvent()
    {
        BuyRentScreenController.stageDialog.close();
    }

    @FXML
    void changeCursor(Event event)
    {
        if(event.getSource()==this.confirmButton)
        {
            this.confirmButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.cancelButton)
        {
            this.cancelButton.setCursor(Cursor.HAND);
        }
    }

    private Callback<DatePicker, DateCell> disableBookedAndPassedDays()
    {
        ArrayList<LocalDate> localDateList = this.getBookedAppointmentDates();
        return (final DatePicker datePicker) ->
            new DateCell()
            {
                @Override
                public void updateItem(LocalDate item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setDisable(empty || item.compareTo(LocalDate.now()) < 0);
                    if (localDateList.contains(item))
                    {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                    }
                }
            };
    }

    private ArrayList<LocalDate> getBookedAppointmentDates()
    {
        ArrayList<LocalDate>bookedAppointmentDates=new ArrayList<>();
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
            String query="select appointment_date from appointments";
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next())
            {
                String date=resultSet.getString(1);
                LocalDate localDate=LocalDate.of(Integer.parseInt(date.split(" ")[0].split("-")[0]),Integer.parseInt(date.split(" ")[0].split("-")[1]),Integer.parseInt(date.split(" ")[0].split("-")[2]));
                bookedAppointmentDates.add(localDate);
            }
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
        return bookedAppointmentDates;
    }
}