package com.pluralsight.util;

import com.pluralsight.model.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConsoleDecorator {
    private static final int WIDTH = 92;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    public static void appHeader(String title, String subtitle) {
        blankLine();
        colorLine("=", CYAN);
        centered(title.toUpperCase(), BOLD + YELLOW);
        centered(subtitle, CYAN);
        colorLine("=", CYAN);
    }

    public static void section(String title) {
        blankLine();
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
        printColored("| " + padRight(title.toUpperCase(), WIDTH - 4) + " |", BOLD + BLUE);
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
    }

    public static void menu(String title, String[] options) {
        section(title);
        for (String option : options) {
            String decoratedOption = option.replace(")", "]");
            if (decoratedOption.length() > 1) {
                decoratedOption = "[" + decoratedOption;
            }
            printColored("|   " + padRight(decoratedOption, WIDTH - 6) + " |", GREEN);
        }
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
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
        printColored("| " + padRight(summary, WIDTH - 4) + " |", CYAN);
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
    }

    public static void transactionTable(String title, ArrayList<Transaction> transactions) {
        section(title);

        if (transactions.isEmpty()) {
            emptyState("No transactions found.");
            return;
        }

        printColored(String.format("| %-12s | %-10s | %-24s | %-18s | %12s |",
                "Date", "Time", "Description", "Vendor", "Amount"), BOLD + CYAN);
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);

        double total = 0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
            String row = String.format("| %-12s | %-10s | %-24s | %-18s | %12s |",
                    transaction.getDateTime().format(DATE_FORMAT),
                    transaction.getDateTime().format(TIME_FORMAT),
                    shorten(transaction.getDescription(), 24),
                    shorten(transaction.getVendor(), 18),
                    formatMoney(transaction.getAmount()));
            printColored(row, transaction.getAmount() < 0 ? RED : GREEN);
        }

        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
        printColored(String.format("| %-73s | %12s |", "Total", formatMoney(total)),
                total < 0 ? BOLD + RED : BOLD + GREEN);
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
    }

    public static void prompt(String prompt) {
        System.out.print(color("> " + prompt + ": ", YELLOW));
    }

    public static void pausePrompt() {
        System.out.print(color("> Press Enter to continue...", YELLOW));
    }

    public static void success(String message) {
        System.out.println();
        printColored("[SUCCESS] " + message, GREEN);
    }

    public static void notice(String message) {
        printColored("[NOTICE] " + message, YELLOW);
    }

    public static void goodbye() {
        blankLine();
        colorLine("=", CYAN);
        centered("THANK YOU FOR USING ACCOUNTING LEDGER", BOLD + YELLOW);
        centered("Manage your money wisely. Have a great day!", GREEN);
        colorLine("=", CYAN);
    }

    public static String formatMoney(double amount) {
        if (amount < 0) {
            return String.format("-$%,.2f", Math.abs(amount));
        }

        return String.format("$%,.2f", amount);
    }

    private static void emptyState(String message) {
        printColored("| " + padRight(message, WIDTH - 4) + " |", YELLOW);
        printColored("+" + repeat("-", WIDTH - 2) + "+", BLUE);
    }

    private static void centered(String text) {
        centered(text, "");
    }

    private static void centered(String text, String color) {
        int padding = Math.max(0, (WIDTH - text.length()) / 2);
        printColored(repeat(" ", padding) + text, color);
    }

    private static void line(String character) {
        System.out.println(repeat(character, WIDTH));
    }

    private static void colorLine(String character, String color) {
        printColored(repeat(character, WIDTH), color);
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

    private static void printColored(String text, String color) {
        System.out.println(color(text, color));
    }

    private static String color(String text, String color) {
        if (color == null || color.isBlank()) {
            return text;
        }

        return color + text + RESET;
    }
}
