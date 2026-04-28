package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AccountingApp {
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        //loop that always runs menu
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
                    //asks the user to enter additional information depending on what choice they chose in main menu
                    String depositDescription;
                    LocalDateTime currentTime;
                    String depositVendor;
                    double depositAmount;
                    Transaction depositTransaction = null;
                    //if d is selected asks questions
                    if (menuSelection.equalsIgnoreCase("D")) {
                        //asks user to enter description
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

                    } else if (menuSelection.equalsIgnoreCase("P")) {
                        //asks user to enter description
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

                        //multiplies depositAmount by -1 to always be a negative number in print out
                        depositTransaction = new Transaction(currentTime, depositDescription, depositVendor, depositAmount * -1);
                    } else if (menuSelection.equalsIgnoreCase("L")) {
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
                        }
                    }
                    //if x is selected ends loop
                    else if (menuSelection.equalsIgnoreCase("X")) {
                        break;
                    }

                    //saves the new transaction to a csv file
                    FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    String line;
                    bufferedWriter.write(String.valueOf(depositTransaction));
                    bufferedWriter.newLine();
                    bufferedWriter.close();

                } catch (Exception e) {
                    System.out.println("ERROR: An unexpected error occurred");
                    e.printStackTrace();

            }


        }
    }


}







    //app returns to menu
    //user picks "x" to exit app





