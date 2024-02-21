package Models;

import java.util.ArrayList;

public class Apartment
{
    private int apartmentID;
    private int area;
    private int price;
    private int ownerSSN;
    private String city;
    private String address;
    private String type;
    private String state;
    private String description;
    private ArrayList<String> pictures;

    public Apartment()
    {
        this.setApartmentID(-1);
        this.setArea(-1);
        this.setPrice(-1);
        this.setOwnerSSN(-1);
        this.setCity(null);
        this.setAddress(null);
        this.setType(null);
        this.setState(null);
        this.setDescription(null);
        this.setPictures(null);
    }

    public Apartment(int apartmentID,int area,int price,int ownerSSN,String address,String city,String type,String state,String description,ArrayList<String> pictures)
    {
        this.setApartmentID(apartmentID);
        this.setArea(area);
        this.setPrice(price);
        this.setOwnerSSN(ownerSSN);
        this.setCity(city);
        this.setAddress(address);
        this.setType(type);
        this.setState(state);
        this.setDescription(description);
        this.setPictures(pictures);
    }

    public Apartment(int apartmentID,int area,int price,int ownerSSN,String address,String city,String type,String state,String description)
    {
        this.setApartmentID(apartmentID);
        this.setArea(area);
        this.setPrice(price);
        this.setOwnerSSN(ownerSSN);
        this.setCity(city);
        this.setAddress(address);
        this.setType(type);
        this.setState(state);
        this.setDescription(description);
    }

    public int getApartmentID()
    {
        return this.apartmentID;
    }

    public void setApartmentID(int apartmentID)
    {
        this.apartmentID = apartmentID;
    }

    public int getArea()
    {
        return this.area;
    }

    public void setArea(int area)
    {
        this.area = area;
    }

    public int getPrice()
    {
        return this.price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getOwnerSSN()
    {
        return this.ownerSSN;
    }

    public void setOwnerSSN(int ownerSSN)
    {
        this.ownerSSN=ownerSSN;
    }

    public String getCity()
    {
        return this.city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getState()
    {
        return this.state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ArrayList<String> getPictures()
    {
        return this.pictures;
    }

    public void setPictures(ArrayList<String> pictures)
    {
        this.pictures = pictures;
    }

    @Override
    public String toString()
    {
        return String.format("City: %s\nAddress: %s\nArea: %d\nType: %s\nPrice: %d($)\n",this.city,this.address,this.area,this.type,this.price);
    }
}