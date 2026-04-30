package com.pluralsight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    //transaction data
    private LocalDateTime dateTime;
    private String description;
    private String vendor;
    private double amount;

    //getters & setters
    public Transaction(LocalDateTime dateTime, String description, String vendor, double amount) {
        this.dateTime = dateTime;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    //Method to turn data into a string
    public String toString(){
        LocalDateTime currentTime = dateTime;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        String formattedDateTime = currentTime.format(dateTimeFormatter);
        return formattedDateTime + "|" + description + "|" + vendor + "|" + amount;
    }

    }