package Controllers;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;

import oracle.jdbc.driver.OracleDriver;

import javafx.event.Event;
import javafx.event.ActionEvent;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.util.Duration;

import java.sql.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Models.Apartment;
import Models.ApartmentForSell;
import Models.ApartmentForRent;
import Interfaces.MyListener;

public class BuyRentScreenController implements Initializable
{
    static String apartmentType;
    static Stage stageDialog;
    private int pictureIndex;
    static Apartment apartment;
    private List<Apartment> apartments;
    private MyListener myListener;

    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button homeButton;
    @FXML
    private TextField searchField;
    @FXML
    private RadioButton city;
    @FXML
    private ToggleGroup searchBy;
    @FXML
    private RadioButton price;
    @FXML
    private Button searchButton;
    @FXML
    private ImageView itemPicture;
    @FXML
    private Button bookButton;
    @FXML
    private Button prev;
    @FXML
    private Button next;
    @FXML
    private TextArea itemInformation;
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane itemPane;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle)
    {
        if(HomeScreenController.user.getType().equals("Admin"))
        {
            this.bookButton.setVisible(false);
        }
        this.setData("select *from apartments where type='"+apartmentType+"' And state='Available'");
    }

    @FXML
    public void backToHomeScreen()
    {
        this.switchScreen("/FXML_Files/HomeScreen.fxml","Home");
    }

    @FXML
    public void searchEvent()
    {
        int price;
        String city,errorMessage="";
        boolean error=false;

        if(searchButton.getText().equalsIgnoreCase("Search"))
        {
            if(this.searchField.getText().isBlank())
            {
                errorMessage+="*Search Field Is Empty.\n";
                error=true;
            }

            if(this.searchBy.getSelectedToggle()!=this.city&&this.searchBy.getSelectedToggle()!=this.price)
            {
                errorMessage+="*Please Specify Search Type.\n -By City Or Price.\n";
                error=true;
            }

            if(!error)
            {
                if(this.searchBy.getSelectedToggle()==this.city)
                {
                    city=this.searchField.getText();
                    this.setData("select *from apartments where type='"+apartmentType+"' And city='"+city+"' And state='Available'");
                    this.searchButton.setText("Cancel");
                }
                else if(this.searchBy.getSelectedToggle()==this.price)
                {
                    try
                    {
                        price=Integer.parseInt(this.searchField.getText());
                        this.setData("select *from apartments where type='"+apartmentType+"' And price<="+price+" And state='Available'");
                        this.searchButton.setText("Cancel");
                    }
                    catch (NumberFormatException numberFormatException)
                    {
                        errorMessage+="*Please Enter Valid Price.\n";
                        error=true;
                    }
                }
            }
            if(error)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText("Search Error");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
        }
        else if(searchButton.getText().equalsIgnoreCase("Cancel"))
        {
            this.setData("select *from apartments where type='"+apartmentType+"' And state='Available'");
            this.searchField.clear();
            this.price.setSelected(false);
            this.city.setSelected(false);
            this.searchButton.setText("Search");
        }
    }

    @FXML
    public void switchPictures(Event event)
    {
        if(event.getSource()==this.next)
        {
            if(this.pictureIndex==this.apartment.getPictures().size()-1)
            {
                this.pictureIndex=0;
            }
            else
            {
                ++this.pictureIndex;
            }
        }
        else
        {
            if(this.pictureIndex==0)
            {
                this.pictureIndex=this.apartment.getPictures().size()-1;
            }
            else
            {
                --this.pictureIndex;
            }
        }
        try
        {
            itemPicture.setImage(new Image(new FileInputStream(apartment.getPictures().get(this.pictureIndex))));
        }
        catch (FileNotFoundException fileNotFoundException)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(SplashController.mainStage);
            alert.setHeaderText("Setting Picture Error!");
            alert.setContentText("Image Not Found.");
            alert.showAndWait();
        }
    }

    @FXML
    public void bookEvent()
    {
        try
        {
            Parent root=FXMLLoader.load(getClass().getResource("/FXML_Files/AssignAppointmentDialog.fxml"));
            Pane pane=new Pane(root);
            stageDialog=new Stage();
            Image icon = new Image("/Images/icon.jfif");
            stageDialog.setTitle("Assign an Appointment");
            stageDialog.setResizable(false);
            stageDialog.initOwner(this.bookButton.getScene().getWindow());
            stageDialog.getIcons().add(icon);
            Scene scene = new Scene(pane);
            stageDialog.setScene(scene);
            double centerXPosition = this.bookButton.getScene().getWindow().getX() + this.bookButton.getScene().getWindow().getWidth()/2d;
            double centerYPosition = this.bookButton.getScene().getWindow().getY() + this.bookButton.getScene().getWindow().getHeight()/2d;
            stageDialog.setOnShowing(ev -> stageDialog.hide());
            stageDialog.setOnShown(ev ->
            {
                stageDialog.setX(centerXPosition - stageDialog.getWidth()/2d);
                stageDialog.setY(centerYPosition - stageDialog.getHeight()/2d);
                stageDialog.show();
            });
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

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.homeButton)
        {
            this.homeButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.searchButton)
        {
            this.searchButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.next)
        {
            this.next.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.prev)
        {
            this.prev.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.bookButton)
        {
            this.bookButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.city)
        {
            this.city.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.price)
        {
            this.price.setCursor(Cursor.HAND);
        }
    }

    public void setData(String query)
    {
        int column = 0;
        int row = 1;

        apartments=new ArrayList<>();
        gridPane.getChildren().clear();
        itemPane.setVisible(false);
        apartment=null;

        try
        {
            this.apartments.addAll(this.getData(query));
            if (this.apartments.size() > 0)
            {
                this.myListener = item ->
                {
                    setChosenApartment(item);
                };
            }
            else
            {
                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(SplashController.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Property-Home");
                alert.setContentText("No Apartments Found.");
                alert.show();
            }
            for (int i = 0; i < this.apartments.size(); i++)
            {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FXML_Files/Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(this.apartments.get(i),this.myListener);

                if (column == 1)
                {
                    column = 0;
                    row++;
                }

                this.gridPane.add(anchorPane, column++, row);
                this.gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                this.gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                this.gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                this.gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                this.gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                this.gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.initOwner(SplashController.mainStage);
            alert.setContentText("An Error Occurred While Setting The Data");
            alert.showAndWait();
            exception.printStackTrace();
        }
    }

    private List getData(String query)
    {
        int apartmentForSellID,apartmentForRentID,apartmentID,area,price,ownerSSN;
        String paymentMechanism,plan,city,address,type,state,description;
        PreparedStatement ps;
        ResultSet resultSet1,resultSet2;
        ArrayList<String> pictures;
        ApartmentForSell apartmentForSell;
        ApartmentForRent apartmentForRent;
        List<ApartmentForSell> apartmentForSellList=new ArrayList<>();
        List<ApartmentForRent> apartmentForRentList=new ArrayList<>();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");
            ps=con.prepareStatement(query);
            resultSet1=ps.executeQuery();
            while(resultSet1.next())
            {
                apartmentID=resultSet1.getInt(1);
                ownerSSN=resultSet1.getInt(2);
                city=resultSet1.getString(3);
                address=resultSet1.getString(4);
                area=resultSet1.getInt(5);
                description=resultSet1.getString(6);
                state=resultSet1.getString(7);
                type=resultSet1.getString(8);
                price=resultSet1.getInt(9);

                query="select *from apartment_pictures where aid="+apartmentID+"";
                ps=con.prepareStatement(query);
                resultSet2=ps.executeQuery();
                pictures=new ArrayList<String>();
                while(resultSet2.next())
                {
                    pictures.add(resultSet2.getString(2));
                }

                query="select *from apartments_for_"+apartmentType+" where aid="+apartmentID+"";
                ps=con.prepareStatement(query);
                resultSet2=ps.executeQuery();
                resultSet2.next();

                if(apartmentType.equalsIgnoreCase("Sell"))
                {
                    apartmentForSellID=resultSet2.getInt(1);
                    paymentMechanism=resultSet2.getString(3);
                    apartmentForSell=new ApartmentForSell(apartmentID, area, price, ownerSSN,address, city, type, state, description, pictures, apartmentForSellID, paymentMechanism);
                    apartmentForSellList.add(apartmentForSell);
                }
                else if(apartmentType.equalsIgnoreCase("Rent"))
                {
                    apartmentForRentID=resultSet2.getInt(1);
                    plan=resultSet2.getString(3);
                    apartmentForRent=new ApartmentForRent(apartmentID, area, price, ownerSSN,address, city, type, state, description, pictures, apartmentForRentID, plan);
                    apartmentForRentList.add(apartmentForRent);
                }
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
        if(apartmentType.equalsIgnoreCase("Sell"))
        {
            return  apartmentForSellList;
        }
        else
        {
            return  apartmentForRentList;
        }
    }

    private void setChosenApartment(Apartment apartment) throws FileNotFoundException
    {
        Image image;

        if(apartment==null||apartment.getPictures()==null||apartment.getPictures().size()==0)
        {
            this.itemPicture.setImage(null);
        }
        else
        {
            image = new Image(new FileInputStream(apartment.getPictures().get(0)));
            if(apartmentType.equalsIgnoreCase("Sell"))
            {
                ApartmentForSell apartmentForSell=(ApartmentForSell)apartment;
                this.itemInformation.setText(apartmentForSell.toString());
            }
            else if(apartmentType.equalsIgnoreCase("Rent"))
            {
                ApartmentForRent apartmentForRent=(ApartmentForRent)apartment;
                this.itemInformation.setText(apartmentForRent.toString());
            }
            this.itemPicture.setImage(image);
            this.apartment=apartment;
            this.pictureIndex=0;
            this.itemPane.setVisible(true);
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