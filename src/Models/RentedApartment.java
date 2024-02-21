package Models;

import java.sql.Date;
import java.time.LocalDate;

public class RentedApartment
{
    private int rentID;
    private int aID;
    private int profitAmount;
    private LocalDate rentDate;

    public RentedApartment()
    {
        this.setRentID(-1);
        this.setAID(-1);
        this.setProfitAmount(-1);
        this.setRentDate(null);
    }

    public RentedApartment(int rentID,int aID,int profitAmount,LocalDate rentDate)
    {
        this.setRentID(rentID);
        this.setAID(aID);
        this.setProfitAmount(profitAmount);
        this.setRentDate(rentDate);
    }

    public void setRentID(int rentID)
    {
        this.rentID = rentID;
    }

    public int getRentID()
    {
        return this.rentID;
    }

    public int getAID()
    {
        return this.aID;
    }

    public void setAID(int aID)
    {
        this.aID = aID;
    }

    public void setProfitAmount(int profitAmount)
    {
        this.profitAmount = profitAmount;
    }

    public int getProfitAmount()
    {
        return this.profitAmount;
    }

    public LocalDate getRentDate()
    {
        return this.rentDate;
    }

    public void setRentDate(LocalDate rentDate)
    {
        this.rentDate = rentDate;
    }
}