package Models;

import java.util.ArrayList;

public class ApartmentForSell extends Apartment
{
    private int apartmentForSellID;
    private String paymentMechanism;

    public ApartmentForSell()
    {
        super();
        this.setApartmentForSellID(-1);
        this.setPaymentMechanism(null);
    }

    public ApartmentForSell(int apartmentID, int area, int price,int ownerSSN ,String address, String city, String type, String state, String description, ArrayList<String> pictures,int apartmentForSellID,String paymentMechanism)
    {
        super(apartmentID,area, price, ownerSSN,address, city,type,state,description,pictures);
        this.setApartmentForSellID(apartmentForSellID);
        this.setPaymentMechanism(paymentMechanism);
    }

    public int getApartmentForSellID()
    {
        return this.apartmentForSellID;
    }

    public void setApartmentForSellID(int apartmentForSellID)
    {
        this.apartmentForSellID = apartmentForSellID;
    }

    public String getPaymentMechanism()
    {
        return this.paymentMechanism;
    }

    public void setPaymentMechanism(String paymentMechanism)
    {
        this.paymentMechanism = paymentMechanism;
    }

    @Override
    public String toString()
    {
        return (super.toString()+String.format("Payment Mechanism: %s\nState: %s\nDescription: %s\n",this.paymentMechanism,this.getState(),this.getDescription()));
    }
}