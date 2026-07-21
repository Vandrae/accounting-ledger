# Accounting Ledger
 
A console-based personal finance tracker written in Java. Records deposits and payments to a CSV file and provides reports over different time ranges, including a custom multi-field search.
 
## Features
 
* Add deposits — record money coming in
* Make payments — record money going out (stored as negative amounts)
* Ledger view — list all transactions, deposits only, or payments only, with a running balance snapshot
* Reports — filter transactions by:
   * Month to date
   * Previous month
   * Year to date
   * Previous year
   * Vendor name
   * Custom search (any combination of date range, description, vendor, and amount — blank fields are skipped)
* Styled console UI — color-coded menus, tables, and a ledger dashboard summary (transaction count, total deposits, total payments, balance)
* Input validation — menu choices, required text fields, and money amounts are all validated before the app accepts them (see [Input Validation](#input-validation))
All transactions persist to `src/main/resources/transactions.csv` so the data survives between runs.
 
## Tech Stack
 
* Java 17
* Maven
* `java.time` for date handling
* File I/O with `BufferedReader` / `BufferedWriter`
* ANSI escape codes for console styling
* Spring Boot *(in progress — see [Spring Boot API](#spring-boot-api))*
## Getting Started
 
### Prerequisites
 
* JDK 17 or later
* Maven (or an IDE like IntelliJ that handles Maven for you)
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
 
## Input Validation
 
All console input is validated at the point of entry, and the user is re-prompted until a valid value is given rather than letting bad data reach the ledger.
 
* **Menu options** — `askMenuOption` only accepts a single character that appears in the list of valid options for that menu. Anything else re-prompts with the list of valid choices.
* **Required text** — `askRequiredText` rejects blank or whitespace-only input (used for fields like vendor and description).
* **Money amounts** — `askMoneyAmount` strips `$` and `,` characters, parses the result as a `double`, and rejects `0` or unparseable input (e.g. letters, empty string) with a friendly message before re-prompting.
```java
private static double askMoneyAmount(String prompt) {
    while (true) {
        ConsoleDecorator.prompt(prompt);
        String answer = input.nextLine().trim().replace("$", "").replace(",", "");
 
        try {
            double amount = Double.parseDouble(answer);
            if (amount != 0) {
                return amount;
            }
            ConsoleDecorator.notice("Amount must be more than 0.");
        } catch (Exception e) {
            ConsoleDecorator.notice("Please enter a valid amount, like 42.50.");
        }
    }
}
```
 
This pattern (loop → validate → re-prompt on failure) is used consistently across menu selection, text entry, and amount entry so invalid data never makes it into a `Transaction` object.
 
## Data Format
 
Transactions are stored in `transactions.csv` using `|` as the delimiter:
 
```
2025-04-15|10:13:25|invoice paid|ACME Corp|1500.00
2025-04-16|14:02:11|office supplies|Staples|-47.89
```
 
Positive amounts are deposits; negative amounts are payments.
 
## Spring Boot API
 
A Spring Boot migration is underway to expose the ledger over REST endpoints (eventual goal: mobile app integration). Currently this is just the application entry point — no endpoints are implemented yet.
 
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication
public class SpringAccountingApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringAccountingApp.class, args);
    }
}
```
 
**Roadmap:**
 
* [ ] `GET /transactions` — list all transactions
* [ ] `GET /transactions/deposits` / `GET /transactions/payments` — filtered views
* [ ] `POST /transactions/deposit` — add a deposit
* [ ] `POST /transactions/payment` — add a payment
* [ ] `GET /reports/{type}` — month-to-date, previous month, year-to-date, previous year, vendor, and custom search reports
* [ ] Persistence layer (MySQL) to replace/complement the CSV file
This section will be filled in with real request/response examples as endpoints are implemented.
 
## Project Structure
 
```
src/main/java/com/pluralsight/
├── CLIAccountingApp.java      # Entry point
├── SpringAccountingApp.java   # Spring Boot entry point (WIP)
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
 
## Contributing
 
Contributions are welcome. This project follows a standard feature-branch workflow:
 
1. **Fork or clone** the repo.
2. **Create a branch off `main`** for your change, using a `type/short-description` naming convention:
   * `feature/vendor-report-export`
   * `bugfix/negative-amount-parsing`
   * `chore/update-readme`
3. **Commit in small, focused chunks** with clear messages (e.g. `Add validation for blank vendor field`).
4. **Push your branch** and open a Pull Request against `main`.
   * Describe what changed and why.
   * Link any related issue.
   * Make sure `mvn compile` (and tests, once added) pass before requesting review.
5. **Request a review** before merging — avoid pushing directly to `main`.
6. Once approved, merge (prefer squash merge to keep history clean) and delete the feature branch.
```
git checkout main
git pull
git checkout -b feature/your-feature-name
# make changes, commit
git push -u origin feature/your-feature-name
# open PR on GitHub
```
 
## Contributors
 
* [Vandrae](https://github.com/Vandrae)
* [CJB2003 ](https://github.com/CJB2003)
* [cosscarlos](https://github.com/Vandrae)
* [edom881](https://github.com/edom881)
* [birukbelay175-bit ](https://github.com/birukbelay175-bit)
## Author
 
[Vandrae](https://github.com/Vandrae)
