# Accounting Ledger

A console-based personal finance tracker written in Java. Records deposits and
payments to a CSV file and provides reports over different time ranges.

## Features

- **Add deposits** — record money coming in
- **Make payments** — record money going out (stored as negative amounts)
- **Ledger view** — list all transactions, deposits only, or payments only
- **Reports** — filter transactions by:
  - Month to date
  - Previous month
  - Year to date
  - Previous year
  - Vendor name

All transactions persist to `src/main/resources/transactions.csv` so the data
survives between runs.

## Tech Stack

- Java
- Maven
- `java.time` for date handling
- File I/O with `BufferedReader` / `BufferedWriter`

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
mvn exec:java -Dexec.mainClass="com.pluralsight.AccountingApp"
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
A) All
D) Deposits
P) Payments
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
├── AccountingApp.java    # Main app, menus, file I/O
└── Transaction.java      # Transaction model
src/main/resources/
└── transactions.csv      # Persisted ledger data
```

## Author

[Vandrae](https://github.com/Vandrae)
