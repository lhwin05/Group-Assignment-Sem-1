import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// Class to represent a single transaction record
class Transaction {
    int transactionId;
    String employeeId;
    LocalDate date;
    LocalTime time;
    String customerName;
    String productId;
    int quantity;
    String outlet;
    double total;

    public Transaction(int transactionId, String employeeId, LocalDate date, LocalTime time, 
                       String customerName, String productId, int quantity, String outlet, double total) {
        this.transactionId = transactionId;
        this.employeeId = employeeId;
        this.date = date;
        this.time = time;
        this.customerName = customerName;
        this.productId = productId;
        this.quantity = quantity;
        this.outlet = outlet;
        this.total = total;
    }

    // Getters for sorting
    public LocalDate getDate() { return date; }
    public double getTotal() { return total; }
    public String getCustomerName() { return customerName; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-10s | %-12s | %-10s | %-15s | %-10s | %-8d | %-10s | %-8.2f |",
                transactionId, employeeId, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                time, customerName, productId, quantity, outlet, total);
    }
}

public class FilterAndSort {

    private static final String CSV_FILE = "Transaction.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Main method
    public static void main(String[] args) {
        List<Transaction> transactions = loadTransactions(CSV_FILE);
        Scanner scanner = new Scanner(System.in);

        if (transactions.isEmpty()) {
            System.out.println("No transactions loaded. Please check the CSV file.");
            return;
        }

        boolean running = true;

        // Loop to keep the program running
        while (running) {
            System.out.println("\n=== Sales History Analyzer ===");
            System.out.println("1. Filter (by Date Range)");
            System.out.println("2. Sort (by Date, Amount, or Name)");
            System.out.println("3. Exit");
            System.out.print("Enter choice (1-3): ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleFilter(transactions, scanner);
                    break;
                case "2":
                    handleSort(transactions, scanner);
                    break;
                case "3":
                    running = false;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        
        scanner.close();
    }

    // Method to handle Filtering
    private static void handleFilter(List<Transaction> transactions, Scanner scanner) {
        System.out.println("\n--- Filter by Date Range ---");
        try {
            System.out.print("Enter Start Date (dd/MM/yyyy): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

            System.out.print("Enter End Date (dd/MM/yyyy): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

            List<Transaction> filtered = new ArrayList<>();
            double totalSales = 0;

            for (Transaction t : transactions) {
                // Check if date is within range (inclusive)
                if ((t.getDate().isEqual(startDate) || t.getDate().isAfter(startDate)) &&
                    (t.getDate().isEqual(endDate) || t.getDate().isBefore(endDate))) {
                    filtered.add(t);
                    totalSales += t.getTotal();
                }
            }

            if (filtered.isEmpty()) {
                System.out.println("No records found in this date range.");
            } else {
                System.out.println("\nFiltered Transactions:");
                printTable(filtered);
                System.out.printf("\nTotal Cumulative Sales: %.2f\n", totalSales);
            }

        } catch (Exception e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    // Method to handle Sorting
    private static void handleSort(List<Transaction> transactions, Scanner scanner) {
        System.out.println("\n--- Sort Transactions ---");
        System.out.println("Choose sorting criteria:");
        System.out.println("1. Date");
        System.out.println("2. Amount (Total)");
        System.out.println("3. Customer Name");
        System.out.print("Enter choice (1-3): ");
        String sortChoice = scanner.nextLine();

        System.out.println("Choose order:");
        System.out.println("1. Ascending (Lowest/Earliest/A-Z)");
        System.out.println("2. Descending (Highest/Latest/Z-A)");
        System.out.print("Enter choice (1 or 2): ");
        String orderChoice = scanner.nextLine();
        boolean isAscending = orderChoice.equals("1");

        List<Transaction> sorted = new ArrayList<>(transactions);

        switch (sortChoice) {
            case "1": // Date
                sorted.sort(Comparator.comparing(Transaction::getDate));
                break;
            case "2": // Amount
                sorted.sort(Comparator.comparingDouble(Transaction::getTotal));
                break;
            case "3": // Name
                sorted.sort(Comparator.comparing(Transaction::getCustomerName));
                break;
            default:
                System.out.println("Invalid sort criteria.");
                return;
        }

        if (!isAscending) {
            Collections.reverse(sorted);
        }

        System.out.println("\nSorted Transactions:");
        printTable(sorted);
    }

    // Method to load transactions from CSV
    private static List<Transaction> loadTransactions(String fileName) {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; } // Skip header
                String[] values = line.split(",");
                if (values.length >= 9) {
                    try {
                        int id = Integer.parseInt(values[0].trim());
                        String empId = values[1].trim();
                        LocalDate date = LocalDate.parse(values[2].trim(), DATE_FORMATTER);
                        LocalTime time = LocalTime.parse(values[3].trim()); 
                        String name = values[4].trim();
                        String prodId = values[5].trim();
                        int qty = Integer.parseInt(values[6].trim());
                        String outlet = values[7].trim();
                        double total = Double.parseDouble(values[8].trim());

                        list.add(new Transaction(id, empId, date, time, name, prodId, qty, outlet, total));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return list;
    }

    // Helper to print table
    private static void printTable(List<Transaction> list) {
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-10s | %-12s | %-10s | %-15s | %-10s | %-8s | %-10s | %-8s |\n", 
                "ID", "Emp ID", "Date", "Time", "Customer", "Prod ID", "Qty", "Outlet", "Total");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        for (Transaction t : list) {
            System.out.println(t);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }
}