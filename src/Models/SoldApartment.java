package Models;

import java.sql.Date;
import java.time.LocalDate;

public class SoldApartment
{
    private int soldID;
    private int aID;
    private int profitAmount;
    private LocalDate soldDate;

    public SoldApartment()
    {
        this.setSoldID(-1);
        this.setAID(-1);
        this.setProfitAmount(-1);
        this.setSoldDate(null);
    }

    public SoldApartment(int soldID,int aID,int profitAmount,LocalDate soldDate)
    {
        this.setSoldID(soldID);
        this.setAID(aID);
        this.setProfitAmount(profitAmount);
        this.setSoldDate(soldDate);
    }

    public void setSoldID(int soldID)
    {
        this.soldID = soldID;
    }

    public int getSoldID()
    {
        return this.soldID;
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

    public LocalDate getSoldDate()
    {
        return this.soldDate;
    }

    public void setSoldDate(LocalDate soldDate)
    {
        this.soldDate = soldDate;
    }
}