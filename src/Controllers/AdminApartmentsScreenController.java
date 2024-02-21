package Controllers;

import Models.Apartment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.*;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import oracle.jdbc.driver.OracleDriver;

import java.util.Objects;
import java.io.IOException;

public class AdminApartmentsScreenController implements Initializable
{
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button homeButton;
    @FXML
    private Button myProfileButton;
    @FXML
    private TableView<Apartment> tableView;
    @FXML
    private TableColumn<Apartment, Integer> apartmentID;
    @FXML
    private TableColumn<Apartment, Integer> area;
    @FXML
    private TableColumn<Apartment, Integer> price;
    @FXML
    private TableColumn<Apartment, String> city;
    @FXML
    private TableColumn<Apartment, String> address;
    @FXML
    private TableColumn<Apartment, String> type;
    @FXML
    private TableColumn<Apartment, String> state;
    @FXML
    private TableColumn<Apartment, String> description;
    @FXML
    private TableColumn<Apartment, Integer> ownerSSN;
    @FXML
    private Button searchButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.tableView.setRowFactory( tv ->
        {
            TableRow<Apartment> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    Apartment rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(SplashController.mainStage);
                    alert.setHeaderText("Apartment Information");
                    alert.setContentText(rowData.toString()+"State: "+rowData.getState()+"\n"+"Description: "+rowData.getDescription()+"\n");
                    alert.show();
                }
            });
            return row ;
        });

        this.fillTableData("select *from apartments");
    }

    @FXML
    public void backToHomeScreen()
    {
        this.switchScreen("/FXML_Files/HomeScreen.fxml","Home");
    }

    @FXML
    public void backToProfileScreen()
    {
        this.switchScreen("/FXML_Files/AdminProfileScreen.fxml","My-Profile");
    }

    @FXML
    public void addEvent()
    {
        try
        {
            Parent root=FXMLLoader.load(getClass().getResource("/FXML_Files/addApartmentDialog.fxml"));
            Pane pane=new Pane(root);
            Stage stageDialog=new Stage();
            Image icon = new Image("/Images/icon.jfif");
            stageDialog.setTitle("Add Apartment Dialog");
            stageDialog.setResizable(false);
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
            AddApartmentDialogController.stage=stageDialog;
            stageDialog.show();
        }
        catch(NullPointerException | IOException exception)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Loading Dialog Error!");
            alert.setContentText("Error Occurred While Loading Add Apartment Dialog.");
            alert.show();
        }
    }

    @FXML
    public void searchEvent()
    {
        if(this.searchButton.getText().equalsIgnoreCase("Cancel"))
        {
            this.changeSearchButtonToCancelButton("Search");
            this.fillTableData("select *from apartments");
        }
        else
        {
            try
            {
                Parent root=FXMLLoader.load(getClass().getResource("/FXML_Files/SearchApartmentDialog.fxml"));
                Pane pane=new Pane(root);
                Stage stageDialog=new Stage();
                Image icon = new Image("/Images/icon.jfif");
                stageDialog.setTitle("Search An Apartment");
                stageDialog.setResizable(false);
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
                SearchApartmentDialogController.stage=stageDialog;
                stageDialog.show();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Dialog Error!");
                alert.setContentText("Error Occurred While Loading Search Apartment Dialog.");
                alert.show();
            }
        }
    }

    @FXML
    public void updateEvent()
    {
        Apartment apartment=tableView.getSelectionModel().getSelectedItem();

        if(apartment!=null&&apartment.getState().equals("Available"))
        {
            try
            {
                UpdateApartmentDialogController.apartment=apartment;
                Parent root=FXMLLoader.load(getClass().getResource("/FXML_Files/UpdateApartmentDialog.fxml"));
                Pane pane=new Pane(root);
                Stage stageDialog=new Stage();
                Image icon = new Image("/Images/icon.jfif");
                stageDialog.setTitle("Update An Apartment");
                stageDialog.setResizable(false);
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
                UpdateApartmentDialogController.stage=stageDialog;
                stageDialog.show();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Loading Dialog Error!");
                alert.setContentText("Error Occurred While Loading Update Apartment Dialog.");
                alert.show();
            }
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("No Apartment Selected Or This Apartment Is Not Available.");
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
        else if(event.getSource()==this.addButton)
        {
            this.addButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.searchButton)
        {
            this.searchButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.updateButton)
        {
            this.updateButton.setCursor(Cursor.HAND);
        }
    }

    public void fillTableData(String query)
    {
        int aID,aArea,aPrice,oSSN;
        String aCity,aAddress,aState,aType,aDescription;
        Apartment apartment;
        ObservableList<Apartment> apartments= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##abdullah", "123456");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                aID=resultSet.getInt(1);
                oSSN=resultSet.getInt(2);
                aCity=resultSet.getString(3);
                aAddress=resultSet.getString(4);
                aArea=resultSet.getInt(5);
                aDescription=resultSet.getString(6);
                aState=resultSet.getString(7);
                aType=resultSet.getString(8);
                aPrice=resultSet.getInt(9);

                apartment=new Apartment(aID, aArea, aPrice, oSSN, aAddress, aCity, aType, aState, aDescription);
                apartments.add(apartment);
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

        this.apartmentID.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("apartmentID"));
        this.area.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("area"));
        this.price.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("price"));
        this.city.setCellValueFactory(new PropertyValueFactory<Apartment, String>("city"));
        this.address.setCellValueFactory(new PropertyValueFactory<Apartment, String>("address"));
        this.type.setCellValueFactory(new PropertyValueFactory<Apartment, String>("type"));
        this.state.setCellValueFactory(new PropertyValueFactory<Apartment, String>("state"));
        this.description.setCellValueFactory(new PropertyValueFactory<Apartment, String>("description"));
        this.ownerSSN.setCellValueFactory(new PropertyValueFactory<Apartment, Integer>("ownerSSN"));
        this.tableView.setItems(apartments);
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
            Scene scene = this.updateButton.getScene();
            root.translateXProperty().set(scene.getWidth());
            this.parentContainer = (AnchorPane) this.updateButton.getScene().getRoot();
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