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
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        //loop that always runs menu
        homeMenu();
    }

    public static void homeMenu() {
        while (true) {
            //Display Home Screen (think about making each option on a println)
            System.out.println("Home Screen");
            System.out.println(" ");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Pick an option from the menu above: ");
            String menuSelection = input.nextLine();


            try {

                if (menuSelection.equalsIgnoreCase("D")) {
                    makeDeposit();
                } else if (menuSelection.equalsIgnoreCase("P")) {
                    makePayment();
                } else if (menuSelection.equalsIgnoreCase("L")) {
                    ledgerMenu();
                }
                //if x is selected ends loop
                else if (menuSelection.equalsIgnoreCase("X")) {
                    break;
                }


            } catch (Exception e) {
                System.out.println("An error occurred");
                e.printStackTrace();

            }


        }

    }

    //loadTransactions methos is responsible for reading the
    //transaction.cvs file and returning an array with the most up to date list

    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            //ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            //ArrayList<String> list = new ArrayList<>();

            String t;
            //while the line isnt empty print it
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


            //calls method from transaction class to make a string
            depositTransaction = new Transaction(currentTime, depositDescription, depositVendor, depositAmount * -1);

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


            //calls method from transaction class to make a string
            depositTransaction = new Transaction(currentTime, depositDescription, depositVendor, depositAmount);

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

    // break into smaller methods
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

            if (ledgerSelection.equalsIgnoreCase("A")) {

                for (int i = 0; i < transactions.size(); i++) {
                    System.out.println(transactions.get(i).toString());
                }

            } else if (ledgerSelection.equalsIgnoreCase("D")) {
                for (int i = 0; i < transactions.size(); i++) {
                    if (transactions.get(i).getAmount() > 0) {
                        System.out.println(transactions.get(i).toString());
                    }
                }

            } else if (ledgerSelection.equalsIgnoreCase("P")) {
                for (int i = 0; i < transactions.size(); i++) {
                    if (transactions.get(i).getAmount() < 0) {
                        System.out.println(transactions.get(i).toString());
                    }
                }
            } else if (ledgerSelection.equalsIgnoreCase("R")) {
                reportsMenu();
            } else if (ledgerSelection.equalsIgnoreCase("H")) {
                break;
            }
        }
    }

    //for future use
    public static void ledgerArray() {
    }

    //for future use
    public static void ledgerDeposit() {
    }

    //for future use
    public static void ledgerPayment() {
    }

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

                for (Transaction t : transactions) {

                    //getting the date and time of the current transaction
                    int dateMonth = t.getDateTime().getMonthValue();
                    int dateYear = t.getDateTime().getYear();

                    // comparing the current transaction to today's date and time
                    if (dateMonth == todayMonth && dateYear == todayYear) {
                        System.out.println(t);
                    }

                }


            } else if (reportsSelection == 2) {

                for (Transaction t : transactions) {

                    //getting the date and time of the current transaction
                    int dateMonth = t.getDateTime().getMonthValue();
                    int dateYear = t.getDateTime().getYear();

                    if (dateMonth == todayMonth - 1 && dateYear == todayYear) {
                        System.out.println(t);
                    }
                }

            } else if (reportsSelection == 3) {
                for (Transaction t : transactions) {

                    //getting the date and time of the current transaction
                    int dateMonth = t.getDateTime().getMonthValue();
                    int dateYear = t.getDateTime().getYear();


                    if (dateYear == todayYear) {
                        System.out.println(t);
                    }
                }


            } else if (reportsSelection == 4) {
                for (Transaction t : transactions) {

                    //getting the date and time of the current transaction
                    int dateMonth = t.getDateTime().getMonthValue();
                    int dateYear = t.getDateTime().getYear();

                    if (dateYear == todayYear - 1) {
                        System.out.println(t);
                    }
                }

            } else if (reportsSelection == 5) {
                vendorSearch(transactions);
            } else if (reportsSelection == 0) {
                break;
            }

        }
    }

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



