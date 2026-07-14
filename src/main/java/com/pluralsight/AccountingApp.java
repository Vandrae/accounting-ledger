package com.pluralsight;

import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {
    //allows user input
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        //loop that always runs menu
        homeMenu();
    }

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

            switch(menuSelection){
                case "D" -> makeDeposit();
                case "P" -> makePayment();
                case "L" -> ledgerMenu();
                case "X" -> {
                    System.out.println("Thank you! Goodbye");
                    appRunning = false;
                }
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

        LocalDate dateToday = LocalDate.now();
        int todayMonth = dateToday.getMonthValue();
        int todayYear = dateToday.getYear();

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
            case 1 -> monthToDate(transactions, todayMonth,todayYear);
            case 2 -> prevMonth(transactions,todayMonth,todayYear);
            case 3 -> yearToDate(transactions, todayYear);
            case 4 -> prevYear(transactions, todayYear);
            case 5 -> vendorSearch(transactions);
            case 6 -> customSearch(transactions);
            case 0 -> {
                System.out.println("Returning...");
                return;
            }
            default -> System.out.println("Not a valid option.");
        }

    }

    //display all transactions from the current month to today
    public static void monthToDate(ArrayList<Transaction> transactions, int todayMonth, int todayYear){
        for (Transaction t : transactions) {
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            if (dateMonth == todayMonth && dateYear == todayYear) {
                System.out.println(t);
            }
        }
    }
    //display all transactions from the previous month
    public static void prevMonth(ArrayList<Transaction> transactions, int todayMonth, int todayYear){
        for (Transaction t : transactions) {
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            if (dateMonth == todayMonth - 1 && dateYear == todayYear) {
                System.out.println(t);
            }
        }
    }
    //display all transactions from the current year to today
    public static void yearToDate(ArrayList<Transaction> transactions, int todayYear){
        for (Transaction t : transactions) {
            int dateYear = t.getDateTime().getYear();

            if (dateYear == todayYear) {
                System.out.println(t);
            }
        }
    }
    //display all transactions from the previous year
    public static void prevYear(ArrayList<Transaction> transactions, int todayYear){
        for (Transaction t : transactions) {
            int dateYear = t.getDateTime().getYear();

            if (dateYear == todayYear - 1) {
                System.out.println(t);
            }
        }
    }
    //display all transactions from a vendor that the user searches for
    public static void vendorSearch(ArrayList<Transaction> transactions) {
        System.out.print("Who is the Vendor? : ");
        String reportVendor = input.nextLine();

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(reportVendor)) {
                System.out.println(t);
            }
        }
    }

    // Custom search bonus
    public static void customSearch(ArrayList<Transaction> transactions) {
        System.out.print("Start Date (yyyy-MM-dd, leave blank to skip): ");
        String startInput = input.nextLine().trim();
        System.out.print("End Date (yyyy-MM-dd, leave blank to skip): ");
        String endInput = input.nextLine().trim();
        System.out.print("Description (leave blank to skip): ");
        String descInput = input.nextLine().trim();
        System.out.print("Vendor (leave blank to skip): ");
        String vendorInput = input.nextLine().trim();
        System.out.print("Amount (leave blank to skip): ");
        String amountInput = input.nextLine().trim();

        LocalDate startDate = null;
        LocalDate endDate = null;
        Double amount = null;

        try {
            if (!startInput.isEmpty()) startDate = LocalDate.parse(startInput);
            if (!endInput.isEmpty()) endDate = LocalDate.parse(endInput);
            if (!amountInput.isEmpty()) amount = Double.parseDouble(amountInput);
        } catch (Exception e) {
            System.out.println("One of your inputs wasn't valid. Please check the date/amount format and try again.");
            return;
        }

        for (Transaction t : transactions) {
            LocalDate tDate = t.getDateTime().toLocalDate();

            if (startDate != null && tDate.isBefore(startDate)) continue;
            if (endDate != null && tDate.isAfter(endDate)) continue;
            if (!descInput.isEmpty() && !t.getDescription().toLowerCase().contains(descInput.toLowerCase())) continue;
            if (!vendorInput.isEmpty() && !t.getVendor().equalsIgnoreCase(vendorInput)) continue;
            if (amount != null && t.getAmount() != amount) continue;

            System.out.println(t);
        }
    }
}



