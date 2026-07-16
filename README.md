# Accounting Ledger

A console-based personal finance tracker written in Java. Records deposits and
payments to a CSV file and provides reports over different time ranges,
including a custom multi-field search.

## Features

- **Add deposits** — record money coming in
- **Make payments** — record money going out (stored as negative amounts)
- **Ledger view** — list all transactions, deposits only, or payments only,
  with a running balance snapshot
- **Reports** — filter transactions by:
  - Month to date
  - Previous month
  - Year to date
  - Previous year
  - Vendor name
  - Custom search (any combination of date range, description, vendor, and
    amount — blank fields are skipped)
- **Styled console UI** — color-coded menus, tables, and a ledger dashboard
  summary (transaction count, total deposits, total payments, balance)

All transactions persist to `src/main/resources/transactions.csv` so the data
survives between runs.

## Tech Stack

- Java 17
- Maven
- `java.time` for date handling
- File I/O with `BufferedReader` / `BufferedWriter`
- ANSI escape codes for console styling

## Getting Started

### Prerequisites
- JDK 17 or later
- Maven (or an IDE like IntelliJ that handles Maven for you)

### Run
Clone the repo and run from your IDE, or from the command line:

```
git clone https://github.com/Vandrae/accounting-ledger.git
cd accounting-ledger
mvn compile
java -cp target/classes com.pluralsight.CLIAccountingApp
```

## How It Works

### Home Menu
```
D) Add Deposit
P) Make Payment
L) Ledger
X) Exit
```

### Ledger Menu
```
A) All Transactions
D) Deposits Only
P) Payments Only
R) Reports
H) Home
```

### Reports Menu
```
1) Month To Date
2) Previous Month
3) Year to Date
4) Previous Year
5) Search by Vendor
6) Custom Search
0) Back
```

## Data Format

Transactions are stored in `transactions.csv` using `|` as the delimiter:

```
2025-04-15|10:13:25|invoice paid|ACME Corp|1500.00
2025-04-16|14:02:11|office supplies|Staples|-47.89
```

Positive amounts are deposits; negative amounts are payments.

## Project Structure

```
src/main/java/com/pluralsight/
├── CLIAccountingApp.java      # Entry point
├── model/
│   └── Transaction.java       # Transaction data model
├── util/
│   ├── FileManager.java       # CSV load/save
│   └── ConsoleDecorator.java  # Styled console output (menus, tables, dashboard)
├── menus/
│   ├── HomeMenu.java          # Home screen: deposits, payments, navigation
│   ├── LedgerMenu.java        # Ledger screen: all/deposits/payments views
│   └── ReportsMenu.java       # Reports screen menu loop
└── service/
    └── ReportService.java     # Report filtering logic (MTD, prev month, YTD, prev year, vendor & custom search)
src/main/resources/
└── transactions.csv           # Persisted ledger data
```

## Author

[Vandrae](https://github.com/Vandrae)
