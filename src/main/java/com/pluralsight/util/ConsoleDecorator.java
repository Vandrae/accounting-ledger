package com.pluralsight.util;

import com.pluralsight.model.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConsoleDecorator {
    private static final int WIDTH = 92;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void appHeader(String title, String subtitle) {
        blankLine();
        line("=");
        centered(title.toUpperCase());
        centered(subtitle);
        line("=");
    }

    public static void section(String title) {
        blankLine();
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
        System.out.println("| " + padRight(title.toUpperCase(), WIDTH - 4) + " |");
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
    }

    public static void menu(String title, String[] options) {
        section(title);
        for (String option : options) {
            String decoratedOption = option.replace(")", "]");
            if (decoratedOption.length() > 1) {
                decoratedOption = "[" + decoratedOption;
            }
            System.out.println("|   " + padRight(decoratedOption, WIDTH - 6) + " |");
        }
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
        blankLine();
    }

    public static void dashboard(ArrayList<Transaction> transactions) {
        double deposits = 0;
        double payments = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits += transaction.getAmount();
            } else {
                payments += transaction.getAmount();
            }
        }

        section("Ledger Snapshot");
        String summary = "Transactions: " + transactions.size()
                + "   Deposits: " + formatMoney(deposits)
                + "   Payments: " + formatMoney(payments)
                + "   Balance: " + formatMoney(deposits + payments);
        System.out.println("| " + padRight(summary, WIDTH - 4) + " |");
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
    }

    public static void transactionTable(String title, ArrayList<Transaction> transactions) {
        section(title);

        if (transactions.isEmpty()) {
            emptyState("No transactions found.");
            return;
        }

        System.out.printf("| %-12s | %-10s | %-24s | %-18s | %12s |%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");

        double total = 0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
            System.out.printf("| %-12s | %-10s | %-24s | %-18s | %12s |%n",
                    transaction.getDateTime().format(DATE_FORMAT),
                    transaction.getDateTime().format(TIME_FORMAT),
                    shorten(transaction.getDescription(), 24),
                    shorten(transaction.getVendor(), 18),
                    formatMoney(transaction.getAmount()));
        }

        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
        System.out.printf("| %-73s | %12s |%n", "Total", formatMoney(total));
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
    }

    public static void prompt(String prompt) {
        System.out.print("> " + prompt + ": ");
    }

    public static void pausePrompt() {
        System.out.print("> Press Enter to continue...");
    }

    public static void success(String message) {
        System.out.println();
        System.out.println("[SUCCESS] " + message);
    }

    public static void notice(String message) {
        System.out.println("[NOTICE] " + message);
    }

    public static void goodbye() {
        blankLine();
        line("=");
        centered("Thank you for using Accounting Ledger. Goodbye!");
        line("=");
    }

    public static String formatMoney(double amount) {
        if (amount < 0) {
            return String.format("-$%,.2f", Math.abs(amount));
        }

        return String.format("$%,.2f", amount);
    }

    private static void emptyState(String message) {
        System.out.println("| " + padRight(message, WIDTH - 4) + " |");
        System.out.println("+" + repeat("-", WIDTH - 2) + "+");
    }

    private static void centered(String text) {
        int padding = Math.max(0, (WIDTH - text.length()) / 2);
        System.out.println(repeat(" ", padding) + text);
    }

    private static void line(String character) {
        System.out.println(repeat(character, WIDTH));
    }

    private static void blankLine() {
        System.out.println();
    }

    private static String padRight(String value, int length) {
        if (value.length() >= length) {
            return value.substring(0, length);
        }

        return value + repeat(" ", length - value.length());
    }

    private static String shorten(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength - 3) + "...";
    }

    private static String repeat(String value, int count) {
        String result = "";
        for (int i = 0; i < count; i++) {
            result += value;
        }
        return result;
    }
}
