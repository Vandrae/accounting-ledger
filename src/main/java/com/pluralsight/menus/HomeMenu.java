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
            ConsoleDecorator.section("Payment");
            depositDescription = askRequiredText("Description");
            depositVendor = askRequiredText("Vendor");
            depositAmount = askMoneyAmount("Amount");

            //today's date and current time
            currentTime = LocalDateTime.now();

            //if they enter a  negative it doesn't multiply a negative by a negative
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

    //method to be used in the home menu
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

            //today's date and current time
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
