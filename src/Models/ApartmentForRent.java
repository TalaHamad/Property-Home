package Models;

import java.util.ArrayList;

public class ApartmentForRent extends Apartment
{
    private int apartmentForRentID;
    private String plan;

    public ApartmentForRent()
    {
        super();
        this.setApartmentForRentID(-1);
        this.setPlan(null);
    }

    public ApartmentForRent(int apartmentID, int area, int price,int ownerSSN, String address, String city, String type, String state, String description, ArrayList<String> pictures, int apartmentForRentID, String plan)
    {
        super(apartmentID,area, price, ownerSSN,address, city,type,state,description,pictures);
        this.setApartmentForRentID(apartmentForRentID);
        this.setPlan(plan);
    }

    public int getApartmentForRentID()
    {
        return this.apartmentForRentID;
    }

    public void setApartmentForRentID(int apartmentForRentID)
    {
        this.apartmentForRentID = apartmentForRentID;
    }

    public String getPlan()
    {
        return this.plan;
    }

    public void setPlan(String plan)
    {
        this.plan = plan;
    }

    @Override
    public String toString()
    {
        return (super.toString()+String.format("Plan: %s\nState: %s\nDescription: %s\n",this.plan,this.getState(),this.getDescription()));
    }
}