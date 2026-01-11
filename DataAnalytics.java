import java.io.*;
import java.util.*;

public class DataAnalytics {

    private final String FILE_PATH = "Transaction.csv";

    public void run(Scanner input) {
        boolean running = true;

        System.out.println("=== Data Analytics ===\n");
        while (running){
            System.out.println("1. Total Sales Per Day");
            System.out.println("2. Most Sold Product");
            System.out.println("3. Average Daily Revenue");
            System.out.print("Choose option: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    totalSalesPerDay();
                    break;
                case 2:
                    mostSoldProduct();
                    break;
                case 3:
                    averageDailyRevenue();
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            System.out.print("\nDo you want to perform another analysis? (Y/N): ");
            String c = input.nextLine();
            if (c.equalsIgnoreCase("N")) {running = false;}
            System.out.println();
        }
    } 
    

    // Total sales per day
    public void totalSalesPerDay() {
        Map<String, Double> dailySales = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip empty lines

                String[] data = line.split(",");
                if (data.length < 10) continue; // skip malformed lines

                String date = data[2];
                double total;
                try {
                    total = Double.parseDouble(data[9]);
                } catch (NumberFormatException e) {
                    continue; // skip invalid total
                }

                dailySales.put(date, dailySales.getOrDefault(date, 0.0) + total);
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction file: " + e.getMessage());
            return;
        }

        System.out.println("\n=== Total Sales Per Day ===");
        for (String date : dailySales.keySet()) {
            System.out.printf("%s : RM %.2f%n", date, dailySales.get(date));
        }
    }

    // Most sold product
    public void mostSoldProduct() {
        Map<String, Integer> productCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length < 10) continue;

                String productID = data[5];
                int qty;
                try {
                    qty = Integer.parseInt(data[6]);
                } catch (NumberFormatException e) {
                    continue; // skip invalid quantity
                }

                productCount.put(productID, productCount.getOrDefault(productID, 0) + qty);
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction file: " + e.getMessage());
            return;
        }

        // Find product with max sold quantity
        String bestProduct = "";
        int maxQty = 0;
        for (Map.Entry<String, Integer> entry : productCount.entrySet()) {
            if (entry.getValue() > maxQty) {
                maxQty = entry.getValue();
                bestProduct = entry.getKey();
            }
        }

        if (!bestProduct.isEmpty()) {
            System.out.println("\nMost Sold Product:");
            System.out.println(bestProduct + " (" + maxQty + " units)");
        } else {
            System.out.println("\nNo valid product data found.");
        }
    }

    // Average daily revenue
    public void averageDailyRevenue() {
        Set<String> days = new HashSet<>();
        double totalRevenue = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");   
                if (data.length < 10) continue;        

                days.add(data[2]); // date
                try {
                    totalRevenue += Double.parseDouble(data[9]);
                } catch (NumberFormatException e) {
                    continue; // skip invalid total
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction file: " + e.getMessage());
            return;
        }

        // total revenue over the total number of days in the hash set (unique values)
        if (!days.isEmpty()) {
            System.out.printf("\nAverage Daily Revenue: RM %.2f%n", totalRevenue / days.size());
        } else {
            System.out.println("\nNo valid transaction data found.");
        }
    }
}


