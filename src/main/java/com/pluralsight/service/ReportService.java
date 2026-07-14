package com.pluralsight.service;

import com.pluralsight.model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/* All report/search filtering logic lives here, separate from menu
printing in AccountingApp. Pure filtering methods are easier to test
and reuse without a Scanner in the way.
 */
public class ReportService {

    /* display all transactions from the current month, up to and including today
    previously matched the whole current month, including future-dated
    entries later in the month. Now also requires date <= today.
     */
    public static void monthToDate(ArrayList<Transaction> transactions) {
        LocalDate today = LocalDate.now();

        for (Transaction t : transactions) {
            LocalDate tDate = t.getDateTime().toLocalDate();
            boolean sameMonthAndYear = tDate.getMonthValue() == today.getMonthValue()
                    && tDate.getYear() == today.getYear();

            if (sameMonthAndYear && !tDate.isAfter(today)) {
                System.out.println(t);
            }
        }
    }

    /* display all transactions from the previous month
    previously used (todayMonth - 1), which breaks in January
    0 never matches, and it doesn't roll into December of last year).
    Now derives the target month/year from today.minusMonths(1).
     */
    public static void prevMonth(ArrayList<Transaction> transactions) {
        LocalDate targetMonth = LocalDate.now().minusMonths(1);

        for (Transaction t : transactions) {
            LocalDate tDate = t.getDateTime().toLocalDate();
            if (tDate.getMonthValue() == targetMonth.getMonthValue()
                    && tDate.getYear() == targetMonth.getYear()) {
                System.out.println(t);
            }
        }
    }

    //display all transactions from the current year to today
    public static void yearToDate(ArrayList<Transaction> transactions) {
        int todayYear = LocalDate.now().getYear();

        for (Transaction t : transactions) {
            if (t.getDateTime().getYear() == todayYear) {
                System.out.println(t);
            }
        }
    }

    //display all transactions from the previous year
    public static void prevYear(ArrayList<Transaction> transactions) {
        int todayYear = LocalDate.now().getYear();

        for (Transaction t : transactions) {
            if (t.getDateTime().getYear() == todayYear - 1) {
                System.out.println(t);
            }
        }
    }

    //display all transactions from a vendor that the user searches for
    public static void vendorSearch(ArrayList<Transaction> transactions, Scanner input) {
        System.out.print("Who is the Vendor? : ");
        String reportVendor = input.nextLine();

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(reportVendor)) {
                System.out.println(t);
            }
        }
    }

    // Custom Search - prompt for every field, skip filters left blank.
    public static void customSearch(ArrayList<Transaction> transactions, Scanner input) {
        System.out.print("Start Date (yyyy-MM-dd, leave blank to skip): ");
        String startInput = input.nextLine().trim();
        System.out.print("End Date (yyyy-MM-dd, leave blank to skip): ");
        String endInput = input.nextLine().trim();
        System.out.print("Description (leave blank to skip): ");
        String descInput = input.nextLine().trim();
        System.out.print("Vendor (leave blank to skip): ");
        String vendorInput = input.nextLine().trim();
        System.out.print("Amount (leave blank to skip): ");
        String amountInput = input.nextLine().trim();

        LocalDate startDate = null;
        LocalDate endDate = null;
        Double amount = null;

        try {
            if (!startInput.isEmpty()) startDate = LocalDate.parse(startInput);
            if (!endInput.isEmpty()) endDate = LocalDate.parse(endInput);
            if (!amountInput.isEmpty()) amount = Double.parseDouble(amountInput);
        } catch (Exception e) {
            System.out.println("One of your inputs wasn't valid. Please check the date/amount format and try again.");
            return;
        }

        for (Transaction t : transactions) {
            LocalDate tDate = t.getDateTime().toLocalDate();

            if (startDate != null && tDate.isBefore(startDate)) continue;
            if (endDate != null && tDate.isAfter(endDate)) continue;
            if (!descInput.isEmpty() && !t.getDescription().toLowerCase().contains(descInput.toLowerCase())) continue;
            if (!vendorInput.isEmpty() && !t.getVendor().equalsIgnoreCase(vendorInput)) continue;
            if (amount != null && t.getAmount() != amount) continue;

            System.out.println(t);
        }
    }
}
