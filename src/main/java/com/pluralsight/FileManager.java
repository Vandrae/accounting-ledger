package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/// Moved the transactions to a different class to declutter main
public class FileManager {

    private static final String FILE_PATH = "src/main/resources/transactions.csv";

    /// Reads every line from transactions.csv and returns it as Transaction objects.
    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] entry = line.split("\\|");
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(entry[0]), LocalTime.parse(entry[1]));
                String description = entry[2];
                String vendor = entry[3];
                double amount = Double.parseDouble(entry[4]);
                transactions.add(new Transaction(dateTime, description, vendor, amount));
            }
            bufferedReader.close();

        } catch (Exception e) {
            System.out.println("An error occurred while loading transactions");
        }
        return transactions;
    }

    /// Appends a single transaction to transactions.csv.
    public static void saveTransaction(Transaction transaction) {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred while saving the transaction");
        }
    }

    /// Convenience method: load + sort newest-first in one call, since
    /// every screen (ledger, reports, custom search) wants it this way.
    public static ArrayList<Transaction> loadTransactionsSortedDesc() {
        ArrayList<Transaction> transactions = loadTransactions();
        transactions.sort(Comparator.comparing(Transaction::getDateTime).reversed());
        return transactions;
    }
}
