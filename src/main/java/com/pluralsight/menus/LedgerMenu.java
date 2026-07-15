package com.pluralsight.menus;

import com.pluralsight.model.Transaction;
import com.pluralsight.util.ConsoleDecorator;
import com.pluralsight.util.FileManager;

import java.util.ArrayList;
import java.util.Scanner;

public class LedgerMenu {

    static Scanner input = HomeMenu.input;
    static ReportsMenu reportsMenu = new ReportsMenu();

    public static void displayLedgerMenu() {

        boolean inLedgerMenu = true;
        while (inLedgerMenu) {

            ArrayList<Transaction> transactions = FileManager.loadTransactionsSortedDesc();

            ConsoleDecorator.dashboard(transactions);
            ConsoleDecorator.menu("Ledger Menu", new String[]{
                    "A) All Transactions",
                    "D) Deposits Only",
                    "P) Payments Only",
                    "R) Reports",
                    "H) Home"
            });

            ConsoleDecorator.prompt("Choose an option");
            String selection = input.nextLine().toUpperCase();
            System.out.println(" ");

            switch (selection) {
                case "A" -> ConsoleDecorator.transactionTable("All Transactions", transactions);
                case "D" -> ConsoleDecorator.transactionTable("Deposits", getDeposits(transactions));
                case "P" -> ConsoleDecorator.transactionTable("Payments", getPayments(transactions));
                case "R" -> ReportsMenu.displayReportMenu();
                case "H" -> {
                    ConsoleDecorator.notice("Returning to the main menu...");
                    inLedgerMenu = false;
                }
                default -> ConsoleDecorator.notice("Not a valid option.");
            }
        }
    }

    private static ArrayList<Transaction> getDeposits(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> deposits = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }

        return deposits;
    }

    private static ArrayList<Transaction> getPayments(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> payments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }

        return payments;
    }
}
