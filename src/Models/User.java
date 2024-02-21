package Models;

import java.time.LocalDate;

public class User extends Person
{
    private int userID;
    private String email;
    private String password;
    private String type;

    public User()
    {
        super();
        this.setUserID(-1);
        this.setEmail(null);
        this.setPassword(null);
        this.setType(null);
    }

    public User(String firstName, String lastName, int ssn, LocalDate dob, String gender, String address, long phoneNumber, int userID, String email, String password, String type)
    {
        super(firstName, lastName, ssn, dob, gender, address,phoneNumber);
        this.setUserID(userID);
        this.setEmail(email);
        this.setPassword(password);
        this.setType(type);
    }

    public int getUserID()
    {
        return this.userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}