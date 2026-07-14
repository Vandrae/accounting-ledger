package com.pluralsight.menus;

import com.pluralsight.model.Transaction;
import com.pluralsight.util.FileManager;

import java.time.LocalDateTime;
import java.util.Scanner;

public class HomeMenu {
    //allows user input
    public static Scanner input = new Scanner(System.in);

    //First menu user sees
    public static void homeMenu() {
        boolean appRunning = true;
        while (appRunning) {
            //Display Home Screen
            System.out.println(" ");
            System.out.println("Home Screen");
            System.out.println(" ");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Pick an option from the menu above: ");
            String menuSelection = input.nextLine().toUpperCase();
            System.out.println("-------------------------------------");
            System.out.println(" ");

            //if the user inputs anything other than D,P,L or X the menu doesnt run
            if (menuSelection.equalsIgnoreCase("D") || menuSelection.equalsIgnoreCase("P") ||
                    menuSelection.equalsIgnoreCase("L") || menuSelection.equalsIgnoreCase("X")
            ){
                switch(menuSelection){
                    case "D" -> makeDeposit();
                    case "P" -> makePayment();
                    case "L" -> LedgerMenu.ledgerMenu();
                    case "X" -> {
                        System.out.println("Thank you! Goodbye");
                        appRunning = false;
                    }
                }
            } else {
                System.out.println("Enter a valid input!");
            }
        }

    }

    //method to be used in the home menu
    public static void makePayment() {
        String depositDescription;
        LocalDateTime currentTime;
        String depositVendor;
        double depositAmount;

        try {
            System.out.print("enter a description: ");
            depositDescription = input.nextLine();
            //asks user to enter Vendor
            System.out.print("Who is the Vendor? : ");
            depositVendor = input.nextLine();

            //asks user to enter amount
            System.out.print("What is the amount? : ");
            depositAmount = input.nextDouble();
            input.nextLine();

            //today's date and current time
            currentTime = LocalDateTime.now();

            //if they enter a  negative it doesn't multiply a negative by a negative
            if (depositAmount >= 0){
                depositAmount *= -1;
            }
            Transaction paymentTransaction = new Transaction(currentTime, depositDescription, depositVendor, depositAmount);
            FileManager.saveTransaction(paymentTransaction);
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
    }

    //method to be used in the home menu
    public static void makeDeposit() {
        String depositDescription;
        LocalDateTime currentTime;
        String depositVendor;
        double depositAmount;

        try {
            System.out.print("enter a description: ");
            depositDescription = input.nextLine();
            //asks user to enter Vendor
            System.out.print("Who is the Vendor? : ");
            depositVendor = input.nextLine();
            //asks user to enter amount
            System.out.print("What is the amount? : ");
            depositAmount = input.nextDouble();
            input.nextLine();

            //today's date and current time
            currentTime = LocalDateTime.now();

            //regardless if they enter negative or positive output will always be a positive
            Transaction depositTransaction = new Transaction(currentTime, depositDescription, depositVendor, Math.abs(depositAmount));
            FileManager.saveTransaction(depositTransaction);
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
    }



}
