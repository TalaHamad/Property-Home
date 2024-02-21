package Models;

import java.time.LocalDate;

public class Appointment
{
    private int appointmentID;
    private int userID;
    private int apartmentID;
    private LocalDate appointmentDate;

    public Appointment()
    {
        this.setAppointmentID(-1);
        this.setUserID(-1);
        this.setApartmentID(-1);
        this.setAppointmentDate(null);
    }

    public Appointment(int appointmentID, int userID, int apartmentID, LocalDate appointmentDate)
    {
        this.setAppointmentID(appointmentID);
        this.setUserID(userID);
        this.setApartmentID(apartmentID);
        this.setAppointmentDate(appointmentDate);
    }

    public int getAppointmentID()
    {
        return this.appointmentID;
    }

    public void setAppointmentID(int appointmentID)
    {
        this.appointmentID = appointmentID;
    }

    public int getUserID()
    {
        return this.userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getApartmentID()
    {
        return this.apartmentID;
    }

    public void setApartmentID(int apartmentID)
    {
        this.apartmentID = apartmentID;
    }

    public LocalDate getAppointmentDate()
    {
        return this.appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate)
    {
        this.appointmentDate = appointmentDate;
    }
}