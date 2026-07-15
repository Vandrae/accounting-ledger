package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {

    // Console colors
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BOLD = "\u001B[1m";
    public static final String RESET = "\u001B[0m";

    // File location
    public static final String TRANSACTION_FILE =
            "src/main/resources/transactions.csv";

    // User input
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcomeScreen();
        homeMenu();
    }

    // Welcome screen
    public static void displayWelcomeScreen() {
        System.out.println();
        System.out.println(BLUE + BOLD);
        System.out.println("======================================================");
        System.out.println("              PERSONAL ACCOUNTING LEDGER");
        System.out.println("======================================================" + RESET);
        System.out.println(
                GREEN + "        Manage your money with confidence!" + RESET);
        System.out.println(
                CYAN + "        Today's Date: " + LocalDate.now() + RESET);
        System.out.println(BLUE
                + "======================================================"
                + RESET);
    }

    // Home menu
    public static void homeMenu() {
        while (true) {
            ArrayList<Transaction> transactions = loadTransactions();

            displayHomeMenu(transactions);

            String menuSelection = input.nextLine().trim();

            switch (menuSelection.toUpperCase()) {
                case "D":
                    makeDeposit();
                    break;

                case "P":
                    makePayment();
                    break;

                case "L":
                    ledgerMenu();
                    break;

                case "X":
                    displayGoodbyeScreen();
                    return;

                default:
                    displayError(
                            "Invalid option. Please choose D, P, L, or X.");
            }
        }
    }

    // Displays the home screen
    public static void displayHomeMenu(
            ArrayList<Transaction> transactions) {

        double currentBalance = getBalance(transactions);
        int depositCount = countDeposits(transactions);
        int paymentCount = countPayments(transactions);

        System.out.println();
        System.out.println(BLUE + BOLD
                + "===================== HOME MENU ====================="
                + RESET);

        if (currentBalance >= 0) {
            System.out.printf(
                    GREEN + " Current Balance: $%,.2f%n" + RESET,
                    currentBalance);
        } else {
            System.out.printf(
                    RED + " Current Balance: -$%,.2f%n" + RESET,
                    Math.abs(currentBalance));
        }

        System.out.println(CYAN
                + " Total Transactions: " + transactions.size()
                + " | Deposits: " + depositCount
                + " | Payments: " + paymentCount
                + RESET);

        System.out.println(
                "-----------------------------------------------------");
        System.out.println(" [D] Add Deposit");
        System.out.println(" [P] Make Payment");
        System.out.println(" [L] View Ledger");
        System.out.println(" [X] Exit");
        System.out.println(BLUE
                + "====================================================="
                + RESET);
        System.out.print(YELLOW + "Choose an option: " + RESET);
    }

    // Loads transactions from the CSV file
    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(TRANSACTION_FILE);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] entry = line.split("\\|");

                if (entry.length < 5) {
                    continue;
                }

                LocalDateTime dateTime = LocalDateTime.of(
                        LocalDate.parse(entry[0]),
                        java.time.LocalTime.parse(entry[1])
                );

                String description = entry[2];
                String vendor = entry[3];
                double amount = Double.parseDouble(entry[4]);

                transactions.add(
                        new Transaction(
                                dateTime,
                                description,
                                vendor,
                                amount
                        )
                );
            }

            bufferedReader.close();

        } catch (Exception e) {
            displayError("Unable to load the transaction file.");
        }

        return transactions;
    }

    // Makes a payment
    public static void makePayment() {
        System.out.println();
        printSectionHeader("MAKE A PAYMENT");

        try {
            System.out.print("Enter a description: ");
            String description = input.nextLine().trim();

            System.out.print("Enter the vendor: ");
            String vendor = input.nextLine().trim();

            double amount = readPositiveAmount("Enter the payment amount: $");

            LocalDateTime currentTime = LocalDateTime.now();

            Transaction paymentTransaction =
                    new Transaction(
                            currentTime,
                            description,
                            vendor,
                            -Math.abs(amount)
                    );

            saveTransaction(paymentTransaction);

            System.out.println();
            System.out.println(GREEN + BOLD
                    + "Payment recorded successfully!" + RESET);
            System.out.printf(
                    YELLOW + "Amount Paid: $%,.2f%n" + RESET,
                    Math.abs(amount));
            System.out.println(
                    CYAN + "Vendor: " + vendor + RESET);

        } catch (Exception e) {
            displayError(
                    "The payment could not be recorded. Please try again.");
        }
    }

    // Adds a deposit
    public static void makeDeposit() {
        System.out.println();
        printSectionHeader("ADD A DEPOSIT");

        try {
            System.out.print("Enter a description: ");
            String description = input.nextLine().trim();

            System.out.print("Enter the source or vendor: ");
            String vendor = input.nextLine().trim();

            double amount = readPositiveAmount("Enter the deposit amount: $");

            LocalDateTime currentTime = LocalDateTime.now();

            Transaction depositTransaction =
                    new Transaction(
                            currentTime,
                            description,
                            vendor,
                            Math.abs(amount)
                    );

            saveTransaction(depositTransaction);

            System.out.println();
            System.out.println(GREEN + BOLD
                    + "Deposit added successfully!" + RESET);
            System.out.printf(
                    GREEN + "Amount Deposited: $%,.2f%n" + RESET,
                    Math.abs(amount));
            System.out.println(
                    CYAN + "Source: " + vendor + RESET);

        } catch (Exception e) {
            displayError(
                    "The deposit could not be saved. Please try again.");
        }
    }

    // Saves one transaction to the CSV file
    public static void saveTransaction(Transaction transaction)
            throws Exception {

        FileWriter fileWriter =
                new FileWriter(TRANSACTION_FILE, true);

        BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

        bufferedWriter.write(transaction.toString());
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    // Ledger menu
    public static void ledgerMenu() {
        while (true) {
            ArrayList<Transaction> transactions = loadTransactions();

            System.out.println();
            System.out.println(PURPLE + BOLD
                    + "==================== LEDGER MENU ===================="
                    + RESET);
            System.out.println(" [A] View All Transactions");
            System.out.println(" [D] View Deposits");
            System.out.println(" [P] View Payments");
            System.out.println(" [R] View Reports");
            System.out.println(" [H] Return Home");
            System.out.println(PURPLE
                    + "====================================================="
                    + RESET);
            System.out.print(YELLOW + "Choose an option: " + RESET);

            String ledgerSelection = input.nextLine().trim();

            switch (ledgerSelection.toUpperCase()) {
                case "A":
                    ledgerAll(transactions);
                    break;

                case "D":
                    ledgerDeposit(transactions);
                    break;

                case "P":
                    ledgerPayment(transactions);
                    break;

                case "R":
                    reportsMenu();
                    break;

                case "H":
                    return;

                default:
                    displayError(
                            "Invalid option. Please choose A, D, P, R, or H.");
            }
        }
    }

    // Displays all transactions
    public static void ledgerAll(
            ArrayList<Transaction> transactions) {

        printSectionHeader("ALL TRANSACTIONS");

        if (transactions.isEmpty()) {
            displayNoResults("No transactions were found.");
            return;
        }

        for (Transaction transaction : transactions) {
            printTransaction(transaction);
        }

        printTransactionSummary(transactions);
    }

    // Displays deposits
    public static void ledgerDeposit(
            ArrayList<Transaction> transactions) {

        printSectionHeader("DEPOSITS");

        int resultCount = 0;
        double total = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                printTransaction(transaction);
                total += transaction.getAmount();
                resultCount++;
            }
        }

        if (resultCount == 0) {
            displayNoResults("No deposits were found.");
        } else {
            System.out.println(GREEN
                    + "-----------------------------------------------------"
                    + RESET);
            System.out.printf(
                    GREEN + BOLD
                            + "Total Deposits: $%,.2f | Entries: %d%n"
                            + RESET,
                    total,
                    resultCount);
        }
    }

    // Displays payments
    public static void ledgerPayment(
            ArrayList<Transaction> transactions) {

        printSectionHeader("PAYMENTS");

        int resultCount = 0;
        double total = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                printTransaction(transaction);
                total += Math.abs(transaction.getAmount());
                resultCount++;
            }
        }

        if (resultCount == 0) {
            displayNoResults("No payments were found.");
        } else {
            System.out.println(RED
                    + "-----------------------------------------------------"
                    + RESET);
            System.out.printf(
                    RED + BOLD
                            + "Total Payments: $%,.2f | Entries: %d%n"
                            + RESET,
                    total,
                    resultCount);
        }
    }

    // Reports menu
    public static void reportsMenu() {
        while (true) {
            ArrayList<Transaction> transactions = loadTransactions();

            LocalDate today = LocalDate.now();
            int todayMonth = today.getMonthValue();
            int todayYear = today.getYear();

            System.out.println();
            System.out.println(BLUE + BOLD
                    + "==================== REPORTS MENU ==================="
                    + RESET);
            System.out.println(" [1] Month To Date");
            System.out.println(" [2] Previous Month");
            System.out.println(" [3] Year To Date");
            System.out.println(" [4] Previous Year");
            System.out.println(" [5] Search By Vendor");
            System.out.println(" [0] Return To Ledger");
            System.out.println(BLUE
                    + "====================================================="
                    + RESET);
            System.out.print(YELLOW + "Choose an option: " + RESET);

            String reportsSelection = input.nextLine().trim();

            switch (reportsSelection) {
                case "1":
                    monthToDate(
                            transactions,
                            todayMonth,
                            todayYear);
                    break;

                case "2":
                    prevMonth(transactions);
                    break;

                case "3":
                    yearToDate(transactions, todayYear);
                    break;

                case "4":
                    prevYear(transactions, todayYear);
                    break;

                case "5":
                    vendorSearch(transactions);
                    break;

                case "0":
                    return;

                default:
                    displayError(
                            "Invalid option. Please select a number from 0 to 5.");
            }
        }
    }

    // Displays current month transactions
    public static void monthToDate(
            ArrayList<Transaction> transactions,
            int todayMonth,
            int todayYear) {

        printSectionHeader("MONTH TO DATE");

        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            int transactionMonth =
                    transaction.getDateTime().getMonthValue();

            int transactionYear =
                    transaction.getDateTime().getYear();

            if (transactionMonth == todayMonth
                    && transactionYear == todayYear) {

                results.add(transaction);
                printTransaction(transaction);
            }
        }

        displayReportSummary(results);
    }

    // Displays previous month transactions
    public static void prevMonth(
            ArrayList<Transaction> transactions) {

        YearMonth previousMonth =
                YearMonth.now().minusMonths(1);

        printSectionHeader(
                "PREVIOUS MONTH - "
                        + previousMonth.getMonth()
                        + " "
                        + previousMonth.getYear());

        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            int transactionMonth =
                    transaction.getDateTime().getMonthValue();

            int transactionYear =
                    transaction.getDateTime().getYear();

            if (transactionMonth == previousMonth.getMonthValue()
                    && transactionYear == previousMonth.getYear()) {

                results.add(transaction);
                printTransaction(transaction);
            }
        }

        displayReportSummary(results);
    }

    // Displays current year transactions
    public static void yearToDate(
            ArrayList<Transaction> transactions,
            int todayYear) {

        printSectionHeader("YEAR TO DATE - " + todayYear);

        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().getYear() == todayYear) {
                results.add(transaction);
                printTransaction(transaction);
            }
        }

        displayReportSummary(results);
    }

    // Displays previous year transactions
    public static void prevYear(
            ArrayList<Transaction> transactions,
            int todayYear) {

        int previousYear = todayYear - 1;

        printSectionHeader("PREVIOUS YEAR - " + previousYear);

        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().getYear() == previousYear) {
                results.add(transaction);
                printTransaction(transaction);
            }
        }

        displayReportSummary(results);
    }

    // Searches transactions by vendor
    public static void vendorSearch(
            ArrayList<Transaction> transactions) {

        System.out.println();
        printSectionHeader("VENDOR SEARCH");

        System.out.print("Enter the vendor name: ");
        String reportVendor = input.nextLine().trim();

        if (reportVendor.isEmpty()) {
            displayError("Vendor name cannot be empty.");
            return;
        }

        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getVendor()
                    .toLowerCase()
                    .contains(reportVendor.toLowerCase())) {

                results.add(transaction);
                printTransaction(transaction);
            }
        }

        if (results.isEmpty()) {
            displayNoResults(
                    "No transactions found for vendor: "
                            + reportVendor);
        } else {
            displayReportSummary(results);
        }
    }

    // Calculates the current balance
    public static double getBalance(
            ArrayList<Transaction> transactions) {

        double balance = 0;

        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }

        return balance;
    }

    // Counts deposits
    public static int countDeposits(
            ArrayList<Transaction> transactions) {

        int count = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                count++;
            }
        }

        return count;
    }

    // Counts payments
    public static int countPayments(
            ArrayList<Transaction> transactions) {

        int count = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                count++;
            }
        }

        return count;
    }

    // Reads and validates a positive amount
    public static double readPositiveAmount(String message) {
        while (true) {
            System.out.print(message);
            String amountText = input.nextLine().trim();

            try {
                double amount = Double.parseDouble(amountText);

                if (amount <= 0) {
                    displayError(
                            "Amount must be greater than zero.");
                } else {
                    return amount;
                }

            } catch (NumberFormatException e) {
                displayError(
                        "Please enter a valid number, such as 25.50.");
            }
        }
    }

    // Prints one transaction
    public static void printTransaction(
            Transaction transaction) {

        if (transaction.getAmount() >= 0) {
            System.out.println(
                    GREEN + transaction + RESET);
        } else {
            System.out.println(
                    RED + transaction + RESET);
        }
    }

    // Displays a summary for all transactions
    public static void printTransactionSummary(
            ArrayList<Transaction> transactions) {

        double deposits = 0;
        double payments = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits += transaction.getAmount();
            } else {
                payments += Math.abs(transaction.getAmount());
            }
        }

        System.out.println(CYAN
                + "-----------------------------------------------------"
                + RESET);
        System.out.printf(
                GREEN + "Total Deposits: $%,.2f%n" + RESET,
                deposits);
        System.out.printf(
                RED + "Total Payments: $%,.2f%n" + RESET,
                payments);
        System.out.printf(
                BLUE + BOLD + "Net Balance: $%,.2f%n" + RESET,
                deposits - payments);
    }

    // Displays report totals
    public static void displayReportSummary(
            ArrayList<Transaction> transactions) {

        if (transactions.isEmpty()) {
            displayNoResults(
                    "No transactions were found for this report.");
            return;
        }

        double total = getBalance(transactions);

        System.out.println(CYAN
                + "-----------------------------------------------------"
                + RESET);
        System.out.println(
                CYAN + "Transactions Found: "
                        + transactions.size() + RESET);
        System.out.printf(
                BLUE + BOLD + "Report Net Total: $%,.2f%n" + RESET,
                total);
    }

    // Prints section headers
    public static void printSectionHeader(String title) {
        System.out.println(CYAN + BOLD
                + "=====================================================");
        System.out.println(" " + title);
        System.out.println(
                "====================================================="
                        + RESET);
    }

    // Displays error messages
    public static void displayError(String message) {
        System.out.println();
        System.out.println(
                RED + BOLD + "[ERROR] " + message + RESET);
    }

    // Displays empty-result messages
    public static void displayNoResults(String message) {
        System.out.println();
        System.out.println(
                YELLOW + "[NO RESULTS] " + message + RESET);
    }

    // Goodbye screen
    public static void displayGoodbyeScreen() {
        System.out.println();
        System.out.println(BLUE + BOLD
                + "=====================================================");
        System.out.println("        THANK YOU FOR USING THE ACCOUNTING APP");
        System.out.println("              Have a wonderful day!");
        System.out.println(
                "====================================================="
                        + RESET);
    }
}