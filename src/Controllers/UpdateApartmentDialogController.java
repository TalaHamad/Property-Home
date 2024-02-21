package Controllers;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.Event;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.io.IOException;

import Models.Apartment;

public class UpdateApartmentDialogController implements Initializable
{
    private int picturesCount;
    private ArrayList<String>apartmentPictures;
    static Stage stage;
    static Apartment apartment;

    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button reUploadPicturesButton;
    @FXML
    private TextField cityField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField areaField;
    @FXML
    private TextField priceField;
    @FXML
    private Text dependingOnLabel;
    @FXML
    private RadioButton firstOne;
    @FXML
    private ToggleGroup dependingON;
    @FXML
    private RadioButton secondOne;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.picturesCount=0;
        this.apartmentPictures=new ArrayList<>();
        this.fillFields();
    }

    @FXML
    void updateEvent()
    {
        if(checkIfDataIsValid())
        {
            int area,price;
            String city,address,type,description,plan=null,paymentMechanism=null;

            area=Integer.parseInt(this.areaField.getText());
            price=Integer.parseInt(this.priceField.getText());
            city=this.cityField.getText();
            address=this.addressField.getText();
            description=this.descriptionField.getText();
            type=apartment.getType();

            if(type.equals("Sell"))
            {
                if(this.dependingON.getSelectedToggle()==this.firstOne)
                {
                    paymentMechanism="Cash";
                }
                else
                {
                    paymentMechanism="Installment";
                }
            }
            else
            {
                if(this.dependingON.getSelectedToggle()==this.firstOne)
                {
                    plan="Monthly";
                }
                else
                {
                    plan="Yearly";
                }
            }

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");

                String query="update apartments set city='"+city+"',address='"+address+"',description='"+description+"',area="+area+",price="+price+" where apartment_id="+apartment.getApartmentID()+"";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                if(type.equals("Sell"))
                {
                    query="update apartments_for_sell set payment_mechanism='"+paymentMechanism+"' where aid="+apartment.getApartmentID()+"";
                }
                else
                {
                    query="update apartments_for_rent set plan='"+plan+"' where aid="+apartment.getApartmentID()+"";
                }

                ps=con.prepareStatement(query);
                ps.executeQuery();

                if(this.picturesCount!=0)
                {
                    query="delete from apartment_pictures where aid="+apartment.getApartmentID()+"";
                    ps=con.prepareStatement(query);
                    ps.executeQuery();

                    for(int i=0;i<this.apartmentPictures.size();i++)
                    {
                        query="insert into apartment_pictures values("+apartment.getApartmentID()+",'"+this.apartmentPictures.get(i)+"')";
                        ps=con.prepareStatement(query);
                        ps.executeQuery();
                    }
                }
                con.close();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(SplashController.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Property-Home");
                alert.setHeaderText(null);
                alert.setContentText("Apartment Updated Successfully.");
                alert.showAndWait();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FXML_Files/AdminApartmentsScreen.fxml"));
                Parent root= fxmlLoader.load();
                AdminApartmentsScreenController adminApartmentsScreenController = fxmlLoader.getController();
                adminApartmentsScreenController.fillTableData("select *from apartments");
                Scene scene=new Scene(root);
                SplashController.mainStage.setScene(scene);
                stage.close();
            }
            catch (SQLException sqlException)
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
                alert.setContentText("Error Occurred While Loading Admin Apartments Screen.");
                alert.show();
            }
        }
    }

    @FXML
    void reUploadPicturesEvent()
    {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(this.updateButton.getScene().getWindow());
        if (file != null)
        {
            String fileAsString = file.toString();
            this.apartmentPictures.add(fileAsString);
            ++this.picturesCount;
            this.reUploadPicturesButton.setText("Re-Upload Pictures"+"("+Integer.toString(this.picturesCount)+")");
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
        if(event.getSource()==this.updateButton)
        {
            this.updateButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.cancelButton)
        {
            this.cancelButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.firstOne)
        {
            this.firstOne.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.secondOne)
        {
            this.secondOne.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.reUploadPicturesButton)
        {
            this.reUploadPicturesButton.setCursor(Cursor.HAND);
        }
    }

    private void fillFields()
    {
        this.cityField.setText(apartment.getCity());
        this.addressField.setText(apartment.getAddress());
        this.descriptionField.setText(apartment.getDescription());
        this.priceField.setText(Integer.toString(apartment.getPrice()));
        this.areaField.setText(Integer.toString(apartment.getArea()));

       try
       {
           DriverManager.registerDriver((new OracleDriver()));
           String url1="jdbc:oracle:thin:@192.168.1.7:1521:xe";
           Connection con = DriverManager.getConnection(url1, "C##abdullah", "123456");
           String query;
           PreparedStatement ps;
           ResultSet resultSet;

           if(apartment.getType().equals("Sell"))
           {
               this.dependingOnLabel.setText("Payment Mechanism");
               this.firstOne.setText("Cash");
               this.secondOne.setText("Installment");

               query="select *from apartments_for_sell where aid="+apartment.getApartmentID()+"";
               ps=con.prepareStatement(query);
               resultSet=ps.executeQuery();
               resultSet.next();

               if(resultSet.getString(3).equals("Cash"))
               {
                   this.firstOne.setSelected(true);
               }
               else
               {
                   this.secondOne.setSelected(true);
               }
           }
           else
           {
               this.dependingOnLabel.setText("Plan");
               this.firstOne.setText("Monthly");
               this.secondOne.setText("Yearly");

               query="select *from apartments_for_rent where aid="+apartment.getApartmentID()+"";
               ps=con.prepareStatement(query);
               resultSet=ps.executeQuery();
               resultSet.next();

               if(resultSet.getString(3).equals("Monthly"))
               {
                   this.firstOne.setSelected(true);
               }
               else
               {
                   this.secondOne.setSelected(true);
               }
           }
           con.close();
       }
       catch (SQLException sqlException)
       {
           Alert alert=new Alert(Alert.AlertType.ERROR);
           alert.initOwner(SplashController.mainStage);
           alert.setHeaderText(null);
           alert.setContentText("This Apartment Is Not Available(Sold/Rented) You Can't Edit Its Information.");
           alert.showAndWait();
       }
    }

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.cityField.getText().isBlank())
        {
            this.cityField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*City Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.cityField.setStyle("");
        }

        if(this.addressField.getText().isBlank())
        {
            this.addressField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Address Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.addressField.setStyle("");
        }

        if(this.areaField.getText().isBlank())
        {
            this.areaField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Area Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.areaField.getText()))
        {
            this.areaField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Area Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.areaField.setStyle("");
        }

        if(this.descriptionField.getText().isBlank())
        {
            this.descriptionField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Description Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.descriptionField.setStyle("");
        }

        if(this.priceField.getText().isBlank())
        {
            this.priceField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Price Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.priceField.getText()))
        {
            this.priceField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Price Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.priceField.setStyle("");
        }

        if(this.picturesCount!=0)
        {
            if(this.picturesCount<4)
            {
                errorMessage+="*Pictures Are Not Enough(You Should Add At Least Four Photos).\n";
                error=true;
            }
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.updateButton.getScene().getWindow());
            alert.setHeaderText("Data Is Invalid!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isNumeric(String string)
    {
        for(char c:string.toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }
}