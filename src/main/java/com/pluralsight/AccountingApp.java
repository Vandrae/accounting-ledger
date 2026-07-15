package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingApp {
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    private static final Scanner input = new Scanner(System.in);
    private static final String TRANSACTIONS_FILE = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        printWelcome();
        homeMenu();
    }

    public static void homeMenu() {
        while (true) {
            printMenu("Home Screen", new String[]{
                    "D) Add Deposit",
                    "P) Make Payment",
                    "L) Ledger",
                    "X) Exit"
            });

            String menuSelection = askMenuOption("Choose an option", "DPLX");

            if (menuSelection.equalsIgnoreCase("D")) {
                addTransaction("Deposit", true);
            } else if (menuSelection.equalsIgnoreCase("P")) {
                addTransaction("Payment", false);
            } else if (menuSelection.equalsIgnoreCase("L")) {
                ledgerMenu();
            } else if (menuSelection.equalsIgnoreCase("X")) {
                printDivider();
                System.out.println("Thanks for using Accounting Ledger. Goodbye!");
                break;
            }
        }
    }

    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(TRANSACTIONS_FILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] entry = line.split("\\|");
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(entry[0]), LocalTime.parse(entry[1]));
                String description = entry[2];
                String vendor = entry[3];
                double amount = Double.parseDouble(entry[4]);
                transactions.add(new Transaction(dateTime, description, vendor, amount));
            }

            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Could not load transactions. Please check " + TRANSACTIONS_FILE + ".");
        }

        return transactions;
    }

    public static void addTransaction(String transactionType, boolean isDeposit) {
        printSection(transactionType);

        String description = askRequiredText("Description");
        String vendor = askRequiredText("Vendor");
        double amount = askMoneyAmount("Amount");

        if (isDeposit) {
            amount = Math.abs(amount);
        } else {
            amount = -Math.abs(amount);
        }

        Transaction transaction = new Transaction(LocalDateTime.now(), description, vendor, amount);

        try {
            FileWriter fileWriter = new FileWriter(TRANSACTIONS_FILE, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();

            System.out.println();
            System.out.println("Saved " + transactionType.toLowerCase() + ": " + vendor + " for " + formatMoney(amount));
        } catch (Exception e) {
            System.out.println("Could not save this transaction. Please try again.");
        }

        pause();
    }

    public static void makePayment() {
        addTransaction("Payment", false);
    }

    public static void makeDeposit() {
        addTransaction("Deposit", true);
    }

    public static void ledgerMenu() {
        ArrayList<Transaction> transactions = loadTransactions();
        while (true) {
            printMenu("Ledger Menu", new String[]{
                    "A) All",
                    "D) Deposits",
                    "P) Payments",
                    "R) Reports",
                    "H) Home"
            });

            String ledgerSelection = askMenuOption("Choose an option", "ADPRH");

            if (ledgerSelection.equalsIgnoreCase("A")) {
                ledgerAll(transactions);
            } else if (ledgerSelection.equalsIgnoreCase("D")) {
                ledgerDeposit(transactions);
            } else if (ledgerSelection.equalsIgnoreCase("P")) {
                ledgerPayment(transactions);
            } else if (ledgerSelection.equalsIgnoreCase("R")) {
                reportsMenu();
            } else if (ledgerSelection.equalsIgnoreCase("H")) {
                break;
            }
        }
    }

    public static void ledgerAll(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
        pause();
    }

    public static void ledgerDeposit(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction.toString());
            }
        }
        pause();
    }

    public static void ledgerPayment(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction.toString());
            }
        }
        pause();
    }

    public static void reportsMenu() {
        ArrayList<Transaction> transactions = loadTransactions();
        LocalDate dateToday = LocalDate.now();
        int todayMonth = dateToday.getMonthValue();
        int todayYear = dateToday.getYear();

        while (true) {
            printMenu("Reports Menu", new String[]{
                    "1) Month To Date",
                    "2) Previous Month",
                    "3) Year to Date",
                    "4) Previous Year",
                    "5) Search by Vendor",
                    "0) Back"
            });

            String reportsSelection = askMenuOption("Choose an option", "123450");

            if (reportsSelection.equals("1")) {
                monthToDate(transactions, todayMonth, todayYear);
            } else if (reportsSelection.equals("2")) {
                prevMonth(transactions, todayMonth, todayYear);
            } else if (reportsSelection.equals("3")) {
                yearToDate(transactions, todayYear);
            } else if (reportsSelection.equals("4")) {
                prevYear(transactions, todayYear);
            } else if (reportsSelection.equals("5")) {
                vendorSearch(transactions);
            } else if (reportsSelection.equals("0")) {
                break;
            }
        }
    }

    public static void monthToDate(ArrayList<Transaction> transactions, int todayMonth, int todayYear) {
        for (Transaction t : transactions) {
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            if (dateMonth == todayMonth && dateYear == todayYear) {
                System.out.println(t);
            }
        }
        pause();
    }

    public static void prevMonth(ArrayList<Transaction> transactions, int todayMonth, int todayYear) {
        for (Transaction t : transactions) {
            int dateMonth = t.getDateTime().getMonthValue();
            int dateYear = t.getDateTime().getYear();

            if (dateMonth == todayMonth - 1 && dateYear == todayYear) {
                System.out.println(t);
            }
        }
        pause();
    }

    public static void yearToDate(ArrayList<Transaction> transactions, int todayYear) {
        for (Transaction t : transactions) {
            int dateYear = t.getDateTime().getYear();

            if (dateYear == todayYear) {
                System.out.println(t);
            }
        }
        pause();
    }

    public static void prevYear(ArrayList<Transaction> transactions, int todayYear) {
        for (Transaction t : transactions) {
            int dateYear = t.getDateTime().getYear();

            if (dateYear == todayYear - 1) {
                System.out.println(t);
            }
        }
        pause();
    }

    public static void vendorSearch(ArrayList<Transaction> transactions) {
        printSection("Vendor Search");
        String reportVendor = askRequiredText("Vendor name");

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(reportVendor)) {
                System.out.println(t);
            }
        }
        pause();
    }

    private static void printWelcome() {
        printDivider();
        System.out.println("ACCOUNTING LEDGER");
        System.out.println("Track deposits, payments, and reports from one simple console.");
        printDivider();
    }

    private static void printMenu(String title, String[] options) {
        printSection(title);
        for (String option : options) {
            System.out.println("  " + option);
        }
        System.out.println();
    }

    private static void printSection(String title) {
        System.out.println();
        printDivider();
        System.out.println(title.toUpperCase());
        printDivider();
    }

    private static String askMenuOption(String prompt, String validOptions) {
        while (true) {
            System.out.print(prompt + ": ");
            String answer = input.nextLine().trim();

            if (answer.length() == 1 && validOptions.toLowerCase().contains(answer.toLowerCase())) {
                return answer;
            }

            System.out.println("Please enter one of these options: " + validOptions);
        }
    }

    private static String askRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String answer = input.nextLine().trim();

            if (!answer.isBlank()) {
                return answer;
            }

            System.out.println(prompt + " cannot be blank.");
        }
    }

    private static double askMoneyAmount(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String answer = input.nextLine().trim().replace("$", "").replace(",", "");

            try {
                double amount = Double.parseDouble(answer);
                if (amount != 0) {
                    return amount;
                }
                System.out.println("Amount must be more than 0.");
            } catch (Exception e) {
                System.out.println("Please enter a valid amount, like 42.50.");
            }
        }
    }

    private static void pause() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        input.nextLine();
    }

    private static void printDivider() {
        System.out.println("================================================================================");
    }

    private static String formatMoney(double amount) {
        if (amount < 0) {
            return String.format("-$%,.2f", Math.abs(amount));
        }

        return String.format("$%,.2f", amount);
    }
}
