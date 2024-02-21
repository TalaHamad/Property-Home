package Models;

import java.time.LocalDate;

public class Person
{
    private String firstName;
    private String lastName;
    private int ssn;
    private LocalDate dob;
    private String gender;
    private String address;
    private long phoneNumber;

    public Person()
    {
        this.setFirstName(null);
        this.setLastName(null);
        this.setSsn(-1);
        this.setDob(null);
        this.setGender(null);
        this.setAddress(null);
        this.setPhoneNumber(-1);
    }

    public Person(String firstName,String lastName,int ssn,LocalDate dob,String gender,String address,long phoneNumber)
    {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setSsn(ssn);
        this.setDob(dob);
        this.setGender(gender);
        this.setAddress(address);
        this.setPhoneNumber(phoneNumber);
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public int getSsn()
    {
        return this.ssn;
    }

    public void setSsn(int ssn)
    {
        this.ssn = ssn;
    }

    public LocalDate getDob()
    {
        return this.dob;
    }

    public void setDob(LocalDate dob)
    {
        this.dob = dob;
    }

    public String getGender()
    {
        return this.gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public long getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}