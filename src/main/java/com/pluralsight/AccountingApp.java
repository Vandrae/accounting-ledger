package com.pluralsight;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AccountingApp {
    public static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        //Display Home Screen (think about making each option on a println)
        System.out.println("Home Screen");
        System.out.println(" ");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Pick an option from the menu above: ");
        String menuSelection = input.next();

        //asks the user to enter additonal information depending on what choice they chose in main menu
        if (menuSelection.equalsIgnoreCase("D")){
            //asks user to enter description
            System.out.print("enter a description: ");
            String depositDescription = input.next();
            //asks user to enter Vendor
            System.out.print("Who is the Vendor? : ");
            String depositVendor = input.next();
            //asks user to enter amount
            System.out.print("What is the amount? : ");
            double depositAmount = input.nextDouble();
        }

    }
    //App auto fills todays date and current time
    //user picks D
    //App auto fills todays date and current time
    //app saves the new transaction to a csv file
    //app returns to menu
    //user picks "x" to exit app



}

