package com.pluralsight.menus;

import com.pluralsight.model.Transaction;
import com.pluralsight.util.ConsoleDecorator;
import com.pluralsight.util.FileManager;

import java.time.LocalDateTime;
import java.util.Scanner;

public class HomeMenu {
    //allows user input
    public static Scanner input = new Scanner(System.in);

    //Create a home menu
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

            //create input validation that accepts only DPLX as valid options
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

    //create a method that allows users to make a payment
    public static void makePayment() {
        String depositDescription;
        LocalDateTime currentTime;
        String depositVendor;
        double depositAmount;

        try {
            ConsoleDecorator.section("Payment");
            depositDescription = askRequiredText("Description");
            depositVendor = askRequiredText("Vendor");
            depositAmount = askMoneyAmount("Amount");

            currentTime = LocalDateTime.now();

            //input validation that doesn't multiply a negative by a negative if one is entered
            if (depositAmount >= 0){
                depositAmount *= -1;
            }
            Transaction paymentTransaction = new Transaction(currentTime, depositDescription, depositVendor, depositAmount);
            FileManager.saveTransaction(paymentTransaction);
            ConsoleDecorator.success("Saved payment: " + depositVendor + " for "
                    + ConsoleDecorator.formatMoney(depositAmount));
        } catch (Exception e) {
            ConsoleDecorator.notice("An error occurred while saving the payment.");
        }
    }

    //create a method that allows users to make a deposit
    public static void makeDeposit() {
        String depositDescription;
        LocalDateTime currentTime;
        String depositVendor;
        double depositAmount;

        try {
            ConsoleDecorator.section("Deposit");
            depositDescription = askRequiredText("Description");
            depositVendor = askRequiredText("Vendor");
            depositAmount = askMoneyAmount("Amount");

            currentTime = LocalDateTime.now();

            //regardless if they enter negative or positive output will always be a positive
            Transaction depositTransaction = new Transaction(currentTime, depositDescription, depositVendor, Math.abs(depositAmount));
            FileManager.saveTransaction(depositTransaction);
            ConsoleDecorator.success("Saved deposit: " + depositVendor + " for "
                    + ConsoleDecorator.formatMoney(Math.abs(depositAmount)));
        } catch (Exception e) {
            ConsoleDecorator.notice("An error occurred while saving the deposit.");
        }
    }

    //create input validation that makes sure menu option is a length of 1 and contains a valid answer
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

    //create input validation that makes sure input is not a blank space
    private static String askRequiredText(String prompt) {
        while (true) {
            ConsoleDecorator.prompt(prompt);
            String answer = input.nextLine().trim();

            if (!answer.isBlank()) {
                return answer;
            }

            ConsoleDecorator.notice(prompt + " cannot be blank.");
        }
    }

    //create input validation that asks for money in the correct format
    private static double askMoneyAmount(String prompt) {
        while (true) {
            ConsoleDecorator.prompt(prompt);
            String answer = input.nextLine().trim().replace("$", "").replace(",", "");

            try {
                double amount = Double.parseDouble(answer);
                if (amount != 0) {
                    return amount;
                }
                ConsoleDecorator.notice("Amount must be more than 0.");
            } catch (Exception e) {
                ConsoleDecorator.notice("Please enter a valid amount, like 42.50.");
            }
        }
    }

}
