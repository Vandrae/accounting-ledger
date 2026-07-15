package com.pluralsight.menus;

import com.pluralsight.util.FileManager;
import com.pluralsight.model.Transaction;

import java.time.LocalDateTime;
import java.util.Scanner;

public class HomeMenu {

    static Scanner input = new Scanner(System.in);
    static LedgerMenu ledgerMenu = new LedgerMenu();

    public static void displayHomeMenu() {
        boolean appRunning = true;
        while (appRunning) {
            System.out.println(" ");
            System.out.println("Home Screen");
            System.out.println(" ");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Pick an option from the menu above: ");
            String selection = input.nextLine().toUpperCase();
            System.out.println("-------------------------------------");
            System.out.println(" ");

            switch (selection) {
                case "D" -> makeDeposit();
                case "P" -> makePayment();
                case "L" -> LedgerMenu.displayLedgerMenu();
                case "X" -> {
                    System.out.println("Thank you! Goodbye");
                    appRunning = false;
                }
                default -> System.out.println("Not a valid option.");
            }
        }
    }

    private static void makePayment() {
        try {
            System.out.print("enter a description: ");
            String description = input.nextLine();
            System.out.print("Who is the Vendor? : ");
            String vendor = input.nextLine();
            System.out.print("What is the amount? : ");
            double amount = input.nextDouble();
            input.nextLine();

            if (amount >= 0) {
                amount *= -1;
            }

            Transaction payment = new Transaction(LocalDateTime.now(), description, vendor, amount);
            FileManager.saveTransaction(payment);
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
    }

    private static void makeDeposit() {
        try {
            System.out.print("enter a description: ");
            String description = input.nextLine();
            System.out.print("Who is the Vendor? : ");
            String vendor = input.nextLine();
            System.out.print("What is the amount? : ");
            double amount = input.nextDouble();
            input.nextLine();

            Transaction deposit = new Transaction(LocalDateTime.now(), description, vendor, Math.abs(amount));
            FileManager.saveTransaction(deposit);
        } catch (Exception e) {
            System.out.println("An error occurred");
        }
    }
}
