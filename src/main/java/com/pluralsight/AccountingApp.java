package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
        while (true) {
            //Display Home Screen
            System.out.println(" ");
            System.out.println("Home Screen");
            System.out.println(" ");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Pick an option from the menu above: ");
            String menuSelection = input.nextLine();

            //try's to do this
            try {

                    //calls methods with code for each function
                if (menuSelection.equalsIgnoreCase("D")) {
                    makeDeposit();

                } else if (menuSelection.equalsIgnoreCase("P")) {
                    makePayment();

                } else if (menuSelection.equalsIgnoreCase("L")) {
                    ledgerMenu();
                }
                //if x is ends program
                else if (menuSelection.equalsIgnoreCase("X")) {
                    break;
                }

               //if it cant do the try prints "An error occurred"
            } catch (Exception e) {
                System.out.println("An error occurred");
                e.printStackTrace();

            }

        }

    }

    //loadTransactions methods is responsible for reading the
    //transaction.cvs file and returning an array with the most up-to-date list
    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            //declare file reader so we don't have to keep declaring it though out program
            //reads from this specific csv file
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String t;

            //while the line isn't empty print it
            while ((t = bufferedReader.readLine()) != null) {

                String[] entry = t.split("\\|");
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(entry[0]), LocalTime.parse(entry[1]));
                String description = entry[2];
                String vendor = entry[3];
                double amount = Double.parseDouble(entry[4]);
                transactions.add(new Transaction(dateTime, description, vendor, amount));

            }
            bufferedReader.close();

        } catch (Exception e) {
            System.out.println("An error occurred");
        }
        return transactions;
    }

    //method to be used in the home menu
    public static void makePayment() {
        String depositDescription;
        LocalDateTime currentTime;
        String depositVendor;
        double depositAmount;
        Transaction depositTransaction = null;
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentTime.format(dateTimeFormatter);


            //if they enter a  negative it doesn't multiply a negative by a negative
            if (depositAmount >= 0){
                depositAmount *= -1;
           }
            depositTransaction = new Transaction(currentTime, depositDescription, depositVendor,depositAmount);

            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String line;
            bufferedWriter.write(String.valueOf(depositTransaction));
            bufferedWriter.newLine();
            bufferedWriter.close();
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
        Transaction depositTransaction = null;
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentTime.format(dateTimeFormatter);


            //regardless if they enter negative or positive output will always be a positive
            depositTransaction = new Transaction(currentTime, depositDescription, depositVendor, Math.abs(depositAmount));

            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String line;
            bufferedWriter.write(String.valueOf(depositTransaction));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred");
        }


    }

    // Level 2 Menu
    public static void ledgerMenu() {
        //prints transactions to console
        ArrayList<Transaction> transactions = loadTransactions();
        while (true) {
            System.out.println("Ledger Menu");
            System.out.println(" ");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Pick an option from the menu above: ");
            String ledgerSelection = input.nextLine();

            //if statement that calls methods for specific jobs
            if (ledgerSelection.equalsIgnoreCase("A")) {
                ledgerAll(transactions);

            } else if (ledgerSelection.equalsIgnoreCase("D")) {
                ledgerDeposit(transactions);

            } else if (ledgerSelection.equalsIgnoreCase("P")) {
                ledgerPayment(transactions);

            } else if (ledgerSelection.equalsIgnoreCase("R")) {
                reportsMenu();

            } else if (ledgerSelection.equalsIgnoreCase("H")) {
                break;
            }
        }
    }

    //method to display all entries on the Ledger
    public static void ledgerAll(ArrayList<Transaction> transactions) {
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println(transactions.get(i).toString());
        }
    }
    //method to display all Deposits
    public static void ledgerDeposit(ArrayList<Transaction> transactions) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getAmount() > 0) {
                System.out.println(transactions.get(i).toString());
            }
        }
    }
    //method to display all Payments
    public static void ledgerPayment(ArrayList<Transaction> transactions) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getAmount() < 0) {
                System.out.println(transactions.get(i).toString());
            }
        }
    }

    //Level 3 Menu
    public static void reportsMenu() {

        //getting most recent list of transactions
        ArrayList<Transaction> transactions = loadTransactions();

        LocalDate dateToday = LocalDate.now();
        int todayMonth = dateToday.getMonthValue();
        int todayYear = dateToday.getYear();

        while (true) {
            System.out.println("Reports Menu");
            System.out.println(" ");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Pick an option from the menu above: ");
            int reportsSelection = input.nextInt();

            if (reportsSelection == 1) {
                monthToDate(transactions, todayMonth, todayYear);

            } else if (reportsSelection == 2) {
                prevMonth(transactions, todayMonth, todayYear);

            } else if (reportsSelection == 3) {
                yearToDate(transactions, todayYear);

            } else if (reportsSelection == 4) {
                prevYear(transactions, todayYear);

            } else if (reportsSelection == 5) {
                vendorSearch(transactions);

            } else if (reportsSelection == 0) {
                break;
            }

        }
    }

    //methods to be used in the reports menu
    //need to call todayMonth in the method like transactions with the data type  so it can be used from reportsMenu
    //display all transactions from the current month to today
    public static void monthToDate(ArrayList<Transaction> transactions, int todayMonth, int todayYear){

        for (Transaction t : transactions) {

            //getting the date and time of the current transaction
            LocalDate dateToday = LocalDate.now();
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            // comparing the current transaction to today's date and time
            if (dateMonth == todayMonth && dateYear == todayYear) {
                System.out.println(t);
            }

        }
    }
    //display all transactions from the previous month
    public static void prevMonth(ArrayList<Transaction> transactions, int todayMonth, int todayYear){
        for (Transaction t : transactions) {

            LocalDate dateToday = LocalDate.now();
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            // prints last month's transactions
            if (dateMonth == todayMonth - 1 && dateYear == todayYear) {
                System.out.println(t);
            }

        }
    }
    //display all transactions from the current year to today
    public static void yearToDate(ArrayList<Transaction> transactions, int todayYear){
        for (Transaction t : transactions) {

            LocalDate dateToday = LocalDate.now();
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            // prints this year's transactions
            if (dateYear == todayYear) {
                System.out.println(t);
            }
        }
    }
    //display all transactions from the previous year
    public static void prevYear(ArrayList<Transaction> transactions, int todayYear){
        for (Transaction t : transactions) {

            LocalDate dateToday = LocalDate.now();
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            // prints last year's transactions
            if (dateYear == todayYear - 1) {
                System.out.println(t);
            }
        }
    }
    //display all transactions from a vendor that the user searches for
    public static void vendorSearch(ArrayList<Transaction> transactions) {
        String reportVendor;
        System.out.print("Who is the Vendor? : ");
        input.nextLine();
        reportVendor = input.nextLine();

        for (Transaction t : transactions) {

            if (t.getVendor().equalsIgnoreCase(reportVendor)) {
                System.out.println(t);
            }


        }
    }
}



