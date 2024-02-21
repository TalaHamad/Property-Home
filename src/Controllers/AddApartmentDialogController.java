package Controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.Cursor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import oracle.jdbc.driver.OracleDriver;

import javafx.event.Event;

import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class AddApartmentDialogController
{
    static Stage stage;
    private int picturesCount=0;
    private ArrayList<String>apartmentPictures=new ArrayList<>();

    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField ownerSSNField;
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
    private RadioButton rent;
    @FXML
    private ToggleGroup type;
    @FXML
    private RadioButton sell;
    @FXML
    private TextField picturesField;
    @FXML
    private Button addPictureButton;
    @FXML
    private Pane pane;
    @FXML
    private Text dependingOnTypeLabel;
    @FXML
    private RadioButton firstOne;
    @FXML
    private ToggleGroup dependingOnType;
    @FXML
    private RadioButton secondOne;

    @FXML
    void addEvent()
    {
        if(this.checkIfDataIsValid())
        {
            int apartmentID=-1,area,price,ownerSSN;
            String city,address,type,description,plan=null,paymentMechanism=null;

            area=Integer.parseInt(this.areaField.getText());
            price=Integer.parseInt(this.priceField.getText());
            ownerSSN=Integer.parseInt(this.ownerSSNField.getText());
            city=this.cityField.getText();
            address=this.addressField.getText();
            description=this.descriptionField.getText();
            if(this.type.getSelectedToggle()==this.sell)
            {
                type="Sell";
                if(this.dependingOnType.getSelectedToggle()==this.firstOne)
                {
                    paymentMechanism="Installment";
                }
                else
                {
                    paymentMechanism="Cash";
                }
            }
            else
            {
                type="Rent";
                if(this.dependingOnType.getSelectedToggle()==this.firstOne)
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

                String query="insert into apartments values(apartmentID_seq.nextVAL,"+ownerSSN+",'"+city+"','"+address+"',"+area+",'"+description+"','Available','"+type+"',"+price+")";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="select APARTMENTID_SEQ.currval from DUAL";
                ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();
                resultSet.next();
                apartmentID=resultSet.getInt(1);

                for(int i=0;i<this.apartmentPictures.size();i++)
                {
                    query="insert into apartment_pictures values("+apartmentID+",'"+this.apartmentPictures.get(i)+"')";
                    ps=con.prepareStatement(query);
                    ps.executeQuery();
                }

                if(type.equals("Sell"))
                {
                    query="insert into apartments_for_sell values(afsID_seq.nextVal,"+apartmentID+",'"+paymentMechanism+"')";
                }
                else
                {
                    query="insert into apartments_for_rent values(afrID_seq.nextVal,"+apartmentID+",'"+plan+"')";
                }
                ps=con.prepareStatement(query);
                ps.executeQuery();
                con.close();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(SplashController.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Property-Home");
                alert.setHeaderText(null);
                alert.setContentText("Apartment Added Successfully.");
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
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                this.reverseOnError(apartmentID);
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(SplashController.mainStage);
                alert.setHeaderText(null);
                alert.setContentText("Owner SSN Not Found, Or You've Entered Duplicated Picture.");
                alert.showAndWait();
            }
            catch (SQLException sqlException)
            {
                this.reverseOnError(apartmentID);
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
    void addPictureEvent()
    {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(this.addButton.getScene().getWindow());
        if (file != null)
        {
            String fileAsString = file.toString();
            this.apartmentPictures.add(fileAsString);
            ++this.picturesCount;
            this.picturesField.setText(Integer.toString(this.picturesCount));
        }
    }

    @FXML
    void cancelEvent()
    {
        stage.close();
    }

    @FXML
    private void typeEvent(Event event)
    {
        if(event.getSource()==this.sell)
        {
            this.dependingOnTypeLabel.setText("Payment Mechanism");
            this.firstOne.setText("Installment");
            this.secondOne.setText("Cash");
            this.pane.setVisible(true);
        }
        else if(event.getSource()==this.rent)
        {
            this.dependingOnTypeLabel.setText("Plan");
            this.firstOne.setText("Monthly");
            this.secondOne.setText("Yearly");
            this.pane.setVisible(true);
        }
    }

    @FXML
    void changeCursor(Event event)
    {
        if(event.getSource()==this.addButton)
        {
            this.addButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.cancelButton)
        {
            this.cancelButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.addPictureButton)
        {
            this.addPictureButton.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.sell)
        {
            this.sell.setCursor(Cursor.HAND);
        }
        else if(event.getSource()==this.rent)
        {
            this.rent.setCursor(Cursor.HAND);
        }
    }

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.ownerSSNField.getText().isBlank())
        {
            this.ownerSSNField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Owner SSN Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.ownerSSNField.getText()))
        {
            this.ownerSSNField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Owner SSN Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.ownerSSNField.setStyle("");
        }

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

        if(this.picturesCount!=4)
        {
            this.picturesField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Pictures Are Not Enough(You Should Add At Least Four Photos).\n";
            error=true;
        }
        else
        {
            this.picturesField.setStyle("");
        }

        if(this.type.getSelectedToggle()!=this.sell&&this.type.getSelectedToggle()!=this.rent)
        {
            errorMessage+="*Please Specify The Apartment Type.\n";
            error=true;
        }

        if(this.type.getSelectedToggle()==this.sell)
        {
            if(this.dependingOnType.getSelectedToggle()!=this.firstOne&&this.dependingOnType.getSelectedToggle()!=this.secondOne)
            {
                errorMessage+="*Please Specify The Payment Mechanism.\n";
                error=true;
            }
        }

        if(this.type.getSelectedToggle()==this.rent)
        {
            if(this.dependingOnType.getSelectedToggle()!=this.firstOne&&this.dependingOnType.getSelectedToggle()!=this.secondOne)
            {
                errorMessage+="*Please Specify The Plan.\n";
                error=true;
            }
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.addButton.getScene().getWindow());
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

    private void reverseOnError(int apartmentID)
    {
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@192.168.1.7:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##abdullah", "123456");

            String query="delete from apartment_pictures where aid="+apartmentID+"";
            PreparedStatement ps=con.prepareStatement(query);
            ps.executeQuery();

            query="delete from apartments_for_sell where aid="+apartmentID+"";
            ps=con.prepareStatement(query);
            ps.executeQuery();

            query="delete from apartments_for_rent where aid="+apartmentID+"";
            ps=con.prepareStatement(query);
            ps.executeQuery();

            query="delete from apartments where apartment_id="+apartmentID+"";
            ps=con.prepareStatement(query);
            ps.executeQuery();

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
    }
}