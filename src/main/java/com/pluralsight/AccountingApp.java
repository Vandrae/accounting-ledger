package com.pluralsight;

import com.pluralsight.model.Transaction;
import com.pluralsight.service.ReportService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    // Console Colors
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    //allows user input
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        //loop that always runs menu
        homeMenu();
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

    // Level 2 Menu
    public static void ledgerMenu() {
        //newest-first list of transactions, sorted by the repository
        ArrayList<Transaction> transactions = FileManager.loadTransactionsSortedDesc();
        boolean appRunning = true;

        System.out.println(" ");
        System.out.println("Ledger Menu");
        System.out.println(" ");
        System.out.println("A) All");
        System.out.println("D) Deposits");
        System.out.println("P) Payments");
        System.out.println("R) Reports");
        System.out.println("H) Home");
        System.out.print("Pick an option from the menu above: ");
        String ledgerSelection = input.nextLine().toUpperCase();
        System.out.println("-------------------------------------");
        System.out.println(" ");

        switch(ledgerSelection){
            case "A" -> ledgerAll(transactions);
            case "D" -> ledgerDeposit(transactions);
            case "P" -> ledgerPayment(transactions);
            case "R" -> reportsMenu();
            case "H" -> {
                System.out.println("Returning to the main menu...");
                return;
            }
        }

    }

    //method to display all entries on the Ledger
    public static void ledgerAll(ArrayList<Transaction> transactions) {
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
    //method to display all Deposits
    public static void ledgerDeposit(ArrayList<Transaction> transactions) {
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                System.out.println(t);
            }
        }
    }
    //method to display all Payments
    public static void ledgerPayment(ArrayList<Transaction> transactions) {
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                System.out.println(t);
            }
        }
    }

    //Level 3 Menu
    public static void reportsMenu() {

        //most recent list of transactions, newest first
        ArrayList<Transaction> transactions = FileManager.loadTransactionsSortedDesc();

        System.out.println(" ");
        System.out.println("Reports Menu");
        System.out.println(" ");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year to Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("6) Custom Search");
        System.out.println("0) Back");
        System.out.print("Pick an option from the menu above: ");

        String selectionText = input.nextLine().trim();
        System.out.println("-------------------------------------");
        System.out.println(" ");

        int reportsSelection;
        try {
            reportsSelection = Integer.parseInt(selectionText);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number from the menu.");
            return;
        }

        switch (reportsSelection){
            case 1 -> ReportService.monthToDate(transactions);
            case 2 -> ReportService.prevMonth(transactions);
            case 3 -> ReportService.yearToDate(transactions);
            case 4 -> ReportService.prevYear(transactions);
            case 5 -> ReportService.vendorSearch(transactions, input);
            case 6 -> ReportService.customSearch(transactions, input);
            case 0 -> {
                System.out.println("Returning...");
                return;
            }
            default -> System.out.println("Not a valid option.");
        }

    }
}



