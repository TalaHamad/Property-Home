package Controllers;

import Models.Apartment;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXML;

import javafx.event.Event;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.Cursor;

import Interfaces.MyListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ItemController
{
    private Apartment apartment;
    private MyListener myListener;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView itemPicture;
    @FXML
    private Label priceLabel;
    @FXML
    private Label stateLabel;

    @FXML
    public void click() throws FileNotFoundException
    {
        this.myListener.onClickListener(this.apartment);
    }

    @FXML
    public void changeCursor(Event event)
    {
        if(event.getSource()==this.anchorPane)
        {
            this.anchorPane.setCursor(Cursor.HAND);
        }
    }

    public void setData(Apartment apartment, MyListener myListener) throws FileNotFoundException
    {
        this.apartment = apartment;
        this.myListener = myListener;
        this.priceLabel.setText(Integer.toString(apartment.getPrice()));
        this.stateLabel.setText(apartment.getState());
        if(apartment==null||apartment.getPictures()==null||apartment.getPictures().size()==0)
        {
            this.itemPicture.setImage(null);
        }
        else
        {
            Image image = new Image(new FileInputStream(apartment.getPictures().get(0)));
            this.itemPicture.setImage(image);
        }
    }
}