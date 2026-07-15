package com.pluralsight.menus;

import com.pluralsight.model.Transaction;
import com.pluralsight.util.ConsoleDecorator;
import com.pluralsight.util.FileManager;

import java.time.LocalDateTime;
import java.util.Scanner;

public class HomeMenu {
    //allows user input
    public static Scanner input = new Scanner(System.in);

    //First menu user sees
    public static void homeMenu() {
        ConsoleDecorator.appHeader(
                "Accounting Ledger",
                "Track deposits, payments, reports, and balances from one simple console.");

        boolean appRunning = true;
        while (appRunning) {
            ConsoleDecorator.menu("Home Screen", new String[]{
                    "D) Add Deposit",
                    "P) Make Payment",
                    "L) Ledger",
                    "X) Exit"
            });

            String menuSelection = askMenuOption("Choose an option", "DPLX");

            switch(menuSelection){
                case "D" -> makeDeposit();
                case "P" -> makePayment();
                case "L" -> LedgerMenu.displayLedgerMenu();
                case "X" -> {
                    ConsoleDecorator.goodbye();
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
            System.out.print("Enter a description: ");
            depositDescription = input.nextLine();
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
            input.nextLine();
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
            System.out.print("Enter a description: ");
            depositDescription = input.nextLine();
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
            input.nextLine();
            System.out.println("An error occurred");
        }
    }

    private static String askMenuOption(String prompt, String validOptions) {
        while (true) {
            ConsoleDecorator.prompt(prompt);
            String answer = input.nextLine().trim().toUpperCase();
            System.out.println();

            if (answer.length() == 1 && validOptions.contains(answer)) {
                return answer;
            }

            ConsoleDecorator.notice("Please enter one of these options: " + validOptions);
        }
    }

}