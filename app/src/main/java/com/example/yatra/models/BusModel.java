package com.example.yatra.models;

import com.example.yatra.utils.StringChunker;

import java.io.Serializable;

public class BusModel implements Serializable {
    private String id;
    private String lisencePlate;
    private String companyName;
    private String startDestination;
    private String endDestination;
    private String journeyDate;
    private String availableSeats;
    private int price;

    @Override
    public String toString() {
        return "BusModel{" +
                "id='" + id + '\'' +
                ", lisencePlate='" + lisencePlate + '\'' +
                ", companyName='" + companyName + '\'' +
                ", startDestination='" + startDestination + '\'' +
                ", endDestination='" + endDestination + '\'' +
                ", journeyDate='" + journeyDate + '\'' +
                ", availableSeats='" + availableSeats + '\'' +
                ", price=" + price +
                ", departureTime='" + departureTime + '\'' +
                ", facilities='" + facilities + '\'' +
                '}';
    }

    private String departureTime;
    private String facilities;

    public BusModel() {
    }

    public BusModel(String id, String lisencePlate, String companyName, String startDestination, String endDestination, String journeyDate, String availableSeats, int price, String departureTime, String facilities) {
        this.id = id;
        this.lisencePlate = lisencePlate;
        this.companyName = companyName;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.journeyDate = journeyDate;
        this.availableSeats = availableSeats;
        this.price = price;
        this.departureTime = departureTime;
        this.facilities = facilities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLisencePlate() {
        return lisencePlate;
    }

    public void setLisencePlate(String lisencePlate) {
        this.lisencePlate = lisencePlate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartDestination() {
        return startDestination;
    }

    public void setStartDestination(String startDestination) {
        this.startDestination = startDestination;
    }

    public String getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(String endDestination) {
        this.endDestination = endDestination;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public int getAvailableSeatCount(){
        String[] strings = StringChunker.chunk(this.availableSeats);
        return  strings.length + 1;
    }
}
