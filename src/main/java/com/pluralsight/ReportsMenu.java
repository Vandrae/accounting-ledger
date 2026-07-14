package com.pluralsight;

import com.pluralsight.model.Transaction;
import com.pluralsight.service.ReportService;

import java.util.ArrayList;
import java.util.Scanner;

public class ReportsMenu {

    //allows user input
    public static Scanner input = new Scanner(System.in);

    //Level 3 Menu
    public static void reportsMenu() {

        //most recent list of transactions, newest first
        ArrayList<Transaction> transactions = FileManager.loadTransactionsSortedDesc();

        System.out.println(" ");
        System.out.println("Reports Menu");
        System.out.println(" ");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year to Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("6) Custom Search");
        System.out.println("0) Back");
        System.out.print("Pick an option from the menu above: ");

        String selectionText = input.nextLine().trim();
        System.out.println("-------------------------------------");
        System.out.println(" ");

        int reportsSelection;
        try {
            reportsSelection = Integer.parseInt(selectionText);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number from the menu.");
            return;
        }

        switch (reportsSelection){
            case 1 -> ReportService.monthToDate(transactions);
            case 2 -> ReportService.prevMonth(transactions);
            case 3 -> ReportService.yearToDate(transactions);
            case 4 -> ReportService.prevYear(transactions);
            case 5 -> ReportService.vendorSearch(transactions, input);
            case 6 -> ReportService.customSearch(transactions, input);
            case 0 -> {
                System.out.println("Returning...");
                return;
            }
            default -> System.out.println("Not a valid option.");
        }

    }

}
