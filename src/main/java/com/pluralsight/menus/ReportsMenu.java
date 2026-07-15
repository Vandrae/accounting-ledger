package com.pluralsight.menus;

import com.pluralsight.model.Transaction;
import com.pluralsight.service.ReportService;
import com.pluralsight.util.ConsoleDecorator;
import com.pluralsight.util.FileManager;

import java.util.ArrayList;
import java.util.Scanner;

public class ReportsMenu {

    static Scanner input = HomeMenu.input;

    public static void displayReportMenu() {

        boolean inReportMenu = true;
        while (inReportMenu) {
            ArrayList<Transaction> transactions = FileManager.loadTransactionsSortedDesc();

            ConsoleDecorator.menu("Reports Menu", new String[]{
                    "1) Month To Date",
                    "2) Previous Month",
                    "3) Year To Date",
                    "4) Previous Year",
                    "5) Search By Vendor",
                    "6) Custom Search",
                    "0) Back"
            });

            ConsoleDecorator.prompt("Choose an option");
            String selectionText = input.nextLine().trim();
            System.out.println(" ");

            int selection;
            try {
                selection = Integer.parseInt(selectionText);
            } catch (NumberFormatException e) {
                ConsoleDecorator.notice("Please enter a number from the menu.");
                continue;
            }

            switch (selection) {
                case 1 -> ReportService.monthToDate(transactions);
                case 2 -> ReportService.prevMonth(transactions);
                case 3 -> ReportService.yearToDate(transactions);
                case 4 -> ReportService.prevYear(transactions);
                case 5 -> ReportService.vendorSearch(transactions, input);
                case 6 -> ReportService.customSearch(transactions, input);
                case 0 -> {
                    ConsoleDecorator.notice("Returning...");
                    inReportMenu = false;
                }
                default -> ConsoleDecorator.notice("Not a valid option.");
            }
        }


    }

}
