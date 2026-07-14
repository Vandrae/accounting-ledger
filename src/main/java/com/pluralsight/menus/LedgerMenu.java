package com.pluralsight.menus;

import com.pluralsight.model.Transaction;
import com.pluralsight.util.FileManager;

import java.util.ArrayList;
import java.util.Scanner;

public class LedgerMenu {

    public static Scanner input = new Scanner(System.in);

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

        //if the user inputs anything other than D,P,L or X the menu doesnt run
        if (ledgerSelection.equalsIgnoreCase("A") || ledgerSelection.equalsIgnoreCase("D") ||
                ledgerSelection.equalsIgnoreCase("P") || ledgerSelection.equalsIgnoreCase("R") ||
                ledgerSelection.equalsIgnoreCase("H")
        ){
            switch(ledgerSelection){
                case "A" -> ledgerAll(transactions);
                case "D" -> ledgerDeposit(transactions);
                case "P" -> ledgerPayment(transactions);
                case "R" -> ReportsMenu.reportsMenu();
                case "H" -> System.out.println("Returning to the main menu...");
            }
        }else {
            System.out.println("Enter a valid input!");
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
}
