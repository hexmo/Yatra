package com.example.yatra.models;

public class BookingModel {
    private String id;
    private String bookerName;
    private String bookerContact;
    private String bookedSeats;
    private String busID;
    private String bookerID;
    private String bookedDate;

    public BookingModel() {
    }

    public BookingModel(String id, String bookerName, String bookerContact, String bookedSeats, String busID, String bookerID, String bookedDate) {
        this.id = id;
        this.bookerName = bookerName;
        this.bookerContact = bookerContact;
        this.bookedSeats = bookedSeats;
        this.busID = busID;
        this.bookerID = bookerID;
        this.bookedDate = bookedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String bookerName) {
        this.bookerName = bookerName;
    }

    public String getBookerContact() {
        return bookerContact;
    }

    public void setBookerContact(String bookerContact) {
        this.bookerContact = bookerContact;
    }

    public String getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(String bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public String getBookerID() {
        return bookerID;
    }

    public void setBookerID(String bookerID) {
        this.bookerID = bookerID;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }
}
