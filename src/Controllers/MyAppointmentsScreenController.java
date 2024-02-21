package Controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;

import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.Cursor;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
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

import Models.Appointment;
import oracle.jdbc.pool.OracleDataSource;

public class MyAppointmentsScreenController implements Initializable
{
    static Stage stageDialog;
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button homeButton;
    @FXML
    private Button myProfileButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<Appointment> tableView;
    @FXML
    private TableColumn<Appointment,Integer> appointmentID;
    @FXML
    private TableColumn<Appointment, LocalDate> appointmentDate;
    @FXML
    private TableColumn<Appointment,Integer> apartmentID;

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle)
    {
        this.fillTableData("select *from appointments where u_id="+HomeScreenController.user.getUserID()+"");
    }

    @FXML
    public void backToHomeScreen()
    {
        this.switchScreen("/FXML_Files/HomeScreen.fxml","Home");
    }

    @FXML
    public void backToMyProfileScreen()
    {
        this.switchScreen("/FXML_Files/ProfileScreen.fxml","My Profile");
    }

    @FXML
    public void searchEvent()
    {
        if(this.searchButton.getText().equalsIgnoreCase("Cancel"))
        {
            this.changeSearchButtonToCancelButton("Search");
            this.fillTableData("select *from appointments where u_id="+HomeScreenController.user.getUserID()+"");
        }
        else
        {
            try
            {
                Parent root=FXMLLoader.load(getClass().getResource("/FXML_Files/SearchAppointmentDialog.fxml"));
                Pane pane=new Pane(root);
                stageDialog=new Stage();
                Image icon = new Image("/Images/icon.jfif");
                stageDialog.setTitle("Search An Appointment");
                stageDialog.setResizable(false);
                stageDialog.initOwner(SplashController.mainStage);
                stageDialog.getIcons().add(icon);
                Scene scene = new Scene(pane);
                stageDialog.setScene(scene);
                double centerXPosition = SplashController.mainStage.getX() + SplashController.mainStage.getWidth()/2d;
                double centerYPosition = SplashController.mainStage.getY() + SplashController.mainStage.getHeight()/2d;
                stageDialog.setOnShowing(ev -> stageDialog.hide());
                stageDialog.setOnShown(ev ->
                {
                    stageDialog.setX(centerXPosition - stageDialog.getWidth()/2d);
                    stageDialog.setY(centerYPosition - stageDialog.getHeight()/2d);
                    stageDialog.show();
                });
                SearchAppointmentDialogController.screenSource="User";
                SearchAppointmentDialogController.stage=stageDialog;
                stageDialog.show();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Dialog Error!");
                alert.setContentText("Error Occurred While Loading Assign Appointment Dialog.");
                alert.show();
            }
        }
    }

    @FXML
    public void deleteEvent()
    {
        Appointment appointment=tableView.getSelectionModel().getSelectedItem();

        if(appointment!=null)
        {
            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url1="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url1, "C##abdullah", "123456");
                String query="delete from appointments where appointment_id="+appointment.getAppointmentID()+"";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="update apartments set state='Available' where apartment_id="+appointment.getApartmentID()+"";
                ps=con.prepareStatement(query);
                ps.executeQuery();
                con.close();

                this.fillTableData("select *from appointments where u_id="+HomeScreenController.user.getUserID()+"");
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
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("No Appointment Selected.");
            alert.showAndWait();
        }
    }

    @FXML
    public void reportEvent()
    {
        try
        {
            OracleDataSource ods=new oracle.jdbc.pool.OracleDataSource();
            HashMap hashMap=new HashMap();
            hashMap.put("userid",HomeScreenController.user.getUserID());
            ods.setURL("jdbc:oracle:thin:@192.168.1.7:1521:xe");
            ods.setUser("C##abdullah");
            ods.setPassword("123456");
            Connection con= ods.getConnection();
            InputStream inputStream=new FileInputStream(new File("src/Reports/Report1/ReportAd.jrxml"));
            JasperDesign jd= JRXmlLoader.load(inputStream);
            JasperReport jr= JasperCompileManager.compileReport(jd);
            JasperPrint jp= JasperFillManager.fillReport(jr,hashMap,con);
            OutputStream output=new FileOutputStream(new File("Report1.pdf"));
            JasperExportManager.exportReportToPdfStream(jp,output);
            output.close();
            inputStream.close();
            con.close();

            Alert alert=new Alert(Alert.AlertType.NONE);
            alert.initOwner(SplashController.mainStage);
            alert.getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Property-Home");
            alert.setHeaderText(null);
            alert.setContentText("Report Generated Successfully.");
            alert.showAndWait();
        }
        catch (Exception exception)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("An Error Occurred While Generating Report.");
            alert.showAndWait();
        }
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.homeButton)
        {
            this.homeButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.myProfileButton)
        {
            this.myProfileButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.searchButton)
        {
            this.searchButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.deleteButton)
        {
            this.deleteButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.reportButton)
        {
            this.reportButton.setCursor(Cursor.HAND);
        }
    }

    public void fillTableData(String query)
    {
        int appointmentID,uID,aID;
        String date;
        LocalDate aDate;
        Appointment appointment;
        ObservableList<Appointment> appointments= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##abdullah", "123456");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                appointmentID=resultSet.getInt(1);
                date=resultSet.getString(2);
                aDate=LocalDate.of(Integer.parseInt(date.split(" ")[0].split("-")[0]),Integer.parseInt(date.split(" ")[0].split("-")[1]),Integer.parseInt(date.split(" ")[0].split("-")[2]));
                uID=resultSet.getInt(3);
                aID=resultSet.getInt(4);
                appointment=new Appointment(appointmentID, uID, aID, aDate);
                appointments.add(appointment);
            }
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

        this.appointmentID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appointmentID"));
        this.appointmentDate.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDate>("appointmentDate"));
        this.apartmentID.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("apartmentID"));
        this.tableView.setItems(appointments);
        this.tableView.refresh();

        tableView.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }

    public void changeSearchButtonToCancelButton(String text)
    {
        this.searchButton.setText(text);
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