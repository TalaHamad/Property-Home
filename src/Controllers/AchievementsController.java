package Controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;

import oracle.jdbc.driver.OracleDriver;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.io.*;
import java.util.Objects;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import Models.Appointment;
import Models.RentedApartment;
import Models.SoldApartment;

public class AchievementsController implements Initializable
{
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button homeButton;
    @FXML
    private TextField profitAmountField;
    @FXML
    private TextField soldApartmentsField;
    @FXML
    private TextField reservedApartmentsField;
    @FXML
    private TextField rentedApartmentsField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.setData();
    }

    @FXML
    public void backToHomeScreen()
    {
        this.switchScreen("/FXML_Files/HomeScreen.fxml","Home");
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.homeButton)
        {
            this.homeButton.setCursor(Cursor.HAND);
        }
    }

    private void setData()
    {
        int soldID,rentID,appointmentID,uID,aID,profitAmount;
        String date;
        LocalDate localDate;
        SoldApartment soldApartment;
        RentedApartment rentedApartment;
        Appointment appointment;
        ArrayList<SoldApartment> soldApartments=new ArrayList<>();
        ArrayList<RentedApartment> rentedApartments=new ArrayList<>();
        ArrayList<Appointment> appointments=new ArrayList<>();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
            String query="select *from sold_apartments";
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                soldID=resultSet.getInt(1);
                date=resultSet.getString(2);
                localDate=LocalDate.of(Integer.parseInt(date.split(" ")[0].split("-")[0]),Integer.parseInt(date.split(" ")[0].split("-")[1]),Integer.parseInt(date.split(" ")[0].split("-")[2]));
                aID=resultSet.getInt(3);
                profitAmount=resultSet.getInt(4);
                soldApartment=new SoldApartment(soldID, aID, profitAmount, localDate);
                soldApartments.add(soldApartment);
            }

            query="select *from Rented_apartments";
            ps=con.prepareStatement(query);
            resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                rentID=resultSet.getInt(1);
                date=resultSet.getString(2);
                localDate=LocalDate.of(Integer.parseInt(date.split(" ")[0].split("-")[0]),Integer.parseInt(date.split(" ")[0].split("-")[1]),Integer.parseInt(date.split(" ")[0].split("-")[2]));
                aID=resultSet.getInt(3);
                profitAmount=resultSet.getInt(4);
                rentedApartment=new RentedApartment(rentID, aID, profitAmount, localDate);
                rentedApartments.add(rentedApartment);
            }

            query="select *from appointments";
            ps=con.prepareStatement(query);
            resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                appointmentID=resultSet.getInt(1);
                date=resultSet.getString(2);
                localDate=LocalDate.of(Integer.parseInt(date.split(" ")[0].split("-")[0]),Integer.parseInt(date.split(" ")[0].split("-")[1]),Integer.parseInt(date.split(" ")[0].split("-")[2]));
                uID=resultSet.getInt(3);
                aID=resultSet.getInt(4);
                appointment=new Appointment(appointmentID, uID, aID, localDate);
                appointments.add(appointment);
            }

            profitAmount=0;

            for(SoldApartment soldApartment1:soldApartments)
            {
                profitAmount+=soldApartment1.getProfitAmount();
            }

            for(RentedApartment rentedApartment1:rentedApartments)
            {
                profitAmount+=rentedApartment1.getProfitAmount();
            }

            this.profitAmountField.setText(Integer.toString(profitAmount));
            this.soldApartmentsField.setText(Integer.toString(soldApartments.size()));
            this.rentedApartmentsField.setText(Integer.toString(rentedApartments.size()));
            this.reservedApartmentsField.setText(Integer.toString(appointments.size()));

            con.close();
        }
        catch(SQLException sqlException)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Data-Base Connection Error!");
            alert.setContentText("An Error Occurred While Connection To Data-Base.");
            alert.showAndWait();
        }
    }

    private void switchScreen(String screen,String screenName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(screen)));
            Scene scene = this.homeButton.getScene();
            root.translateXProperty().set(scene.getWidth());
            this.parentContainer = (AnchorPane) this.homeButton.getScene().getRoot();
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