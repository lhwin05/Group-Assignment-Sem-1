import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FilterAndSort {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Main menu
    public static void main_menu(Scanner scanner) {
        List<Transaction> transactions = Transaction.readCsv();

        boolean running = true;
        while (running) {
            System.out.println("\n=== Sales History Analyzer ===");
            System.out.println("1. Filter (by Date Range)");
            System.out.println("2. Sort (by Date, Amount, or Name)");
            System.out.println("3. Exit");
            System.out.print("Enter choice (1-3): ");
            
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleFilter(transactions, scanner);
                    break;
                case 2:
                    handleSort(transactions, scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting filter & sort");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    // 1. Filtering
    private static void handleFilter(List<Transaction> transactions, Scanner scanner) {
        scanner.nextLine();
        System.out.println("\n--- Filter by Date Range ---");
        try {
            System.out.print("Enter Start Date (yyyy-MM-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

            System.out.print("Enter End Date (yyyy-MM-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

            List<Transaction> filtered = new ArrayList<>();
            double totalSales = 0;

            for (Transaction t : transactions) {
                LocalDate date = LocalDate.parse(t.get_date(), DATE_FORMATTER);

                // Check if date is within range (inclusive)  (>= start && <= end)
                if ((date.isEqual(startDate) || date.isAfter(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate))) {
                    filtered.add(t);
                    totalSales += t.get_total();
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
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    // 2. Sorting
    private static void handleSort(List<Transaction> transactions, Scanner scanner) {
        scanner.nextLine();
        System.out.println("\n--- Sort Transactions ---");
        System.out.println("Choose sorting criteria:");
        System.out.println("1. Date");
        System.out.println("2. Amount (Total)");
        System.out.println("3. Customer Name");
        System.out.print("\nEnter choice (1-3): ");
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
                sorted.sort(Comparator.comparing(t -> LocalDate.parse(t.get_date(), DATE_FORMATTER)));
                break;
            case "2": // Amount
                sorted.sort(Comparator.comparingDouble(Transaction::get_total));
                break;
            case "3": // Name
                sorted.sort(Comparator.comparing(Transaction::get_customer_name));
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

    // 3. Render table
    private static void printTable(List<Transaction> list) {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-10s | %-12s | %-8s | %-15s | %-10s | %-5s | %-12s | %-10s | %-10s |\n", 
                "ID", "Emp ID", "Date", "Time", "Customer", "Prod ID", "Qty", "Payment method", "Outlet", "Total");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        for (Transaction t : list) {
            System.out.println(t);  // this calls toString()
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
    }

    public static void main(String[] args){

    }
}