package com.pluralsight.menus;

import com.pluralsight.util.FileManager;
import com.pluralsight.model.Transaction;

import java.util.ArrayList;
import java.util.Scanner;

public class LedgerMenu {

    static Scanner input = new Scanner(System.in);
    static ReportsMenu reportsMenu = new ReportsMenu();

    public static void displayLedgerMenu() {
        ArrayList<Transaction> transactions = FileManager.loadTransactionsSortedDesc();

        System.out.println(" ");
        System.out.println("Ledger Menu");
        System.out.println(" ");
        System.out.println("A) All");
        System.out.println("D) Deposits");
        System.out.println("P) Payments");
        System.out.println("R) Reports");
        System.out.println("H) Home");
        System.out.print("Pick an option from the menu above: ");
        String selection = input.nextLine().toUpperCase();
        System.out.println("-------------------------------------");
        System.out.println(" ");

        switch (selection) {
            case "A" -> transactions.forEach(System.out::println);
            case "D" -> transactions.stream().filter(t -> t.getAmount() > 0).forEach(System.out::println);
            case "P" -> transactions.stream().filter(t -> t.getAmount() < 0).forEach(System.out::println);
            case "R" -> reportsMenu.displayReportMenu();
            case "H" -> System.out.println("Returning to the main menu...");
            default -> System.out.println("Not a valid option.");
        }
    }
}