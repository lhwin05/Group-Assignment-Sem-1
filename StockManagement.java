import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockManagement {

    public static void main_menu(Scanner sc, CurrentEmployee current){
        System.out.println("=== Stock Management ===\n");
        System.out.println("1. Stock Transfer ");
        System.out.println("2. Stock Count ");
        
        int choice;
        while (true){
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                if (choice != 1 && choice != 2) {
                    System.out.println("Invalid input! "); 
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
        
        switch (choice){
            case 1:
                stockTransfer(sc, current);
                break;
            case 2:
                stockCount(sc);
                break;
        }
    }

    // 1. Stock count
    private static void stockCount(Scanner sc) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int totalMiss = 0;
        int totalTally = 0;
        int totalCheck = 0;

        try{
            int outlet_choice;
            while (true){
                System.out.println("Outlet Location:");
                System.out.println("1. KLCC     2. Lot_10");
                try {
                    outlet_choice = Integer.parseInt(sc.nextLine().trim());
                    if (outlet_choice != 1 && outlet_choice != 2){
                        System.out.println("Invalid input! "); 
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            }
            String outlet = "";
            switch (outlet_choice){
                case 1:
                    outlet = "KLCC";
                    break;
                case 2:
                    outlet = "Lot_10";
                    break;
            }

            String countType = "";

            while (true) {
                System.out.println("Morning OR Night Stock Count");
                countType = sc.nextLine().trim();

                if (countType.equalsIgnoreCase("Morning")) {
                    countType = "Morning";
                    break;
                } else if (countType.equalsIgnoreCase("Night")) {
                    countType = "Night";
                    break;
                } else {
                    System.out.println("Try again.\n");
                }
            }

            // Header
            System.out.println("=== " + countType + " Stock Count ===");
            System.out.println("Date: " + date.format(dateForm));
            System.out.println("Time: " + time.format(timeForm));

            File file = new File("Inventory.csv");
            if (!file.exists()) {
                System.out.println("Inventory.csv not found.");
                return;
            }

            try (Scanner ScanInventory = new Scanner(file)) {
                if (ScanInventory.hasNextLine()) {
                    ScanInventory.nextLine();
                }

                while (ScanInventory.hasNextLine()) {
                    String line = ScanInventory.nextLine();
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(",");
                    
                    int stockCount = 0;
                    String id = parts[0];
                    int Lot_10 = Integer.parseInt(parts[1].trim());
                    int KLCC = Integer.parseInt(parts[2].trim());

                    if (outlet.equals("Lot_10")) {
                        stockCount = Lot_10;
                    } else if (outlet.equals("KLCC")) {
                        stockCount = KLCC;
                    } else {
                        System.out.println("Invalid Location.");
                        return; 
                    }

                    System.out.print("Model: " + id + " -- Counted: ");
                    
                    int counted = 0;
                    try {
                        counted = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Skipping item.");
                        continue;
                    }

                    if (counted == stockCount) {
                        System.out.println("Store Record: " + stockCount);
                        System.out.println("Stock tally correct.\n");
                        totalTally++;
                    } else {
                        System.out.println("Store Record: " + stockCount);
                        int diff = Math.abs(counted - stockCount);
                        System.out.println("! Mismatch detected (" + diff + " unit difference.)\n");
                        totalMiss++;
                    }
                    totalCheck++;
                }

                System.out.println("Total Models Checked: " + totalCheck);
                System.out.println("Tally Correct: " + totalTally);
                System.out.println("Mismatches: " + totalMiss);
                System.out.println(countType + " stock count completed.");

                if (totalMiss > 0) {
                    System.out.println("Warning: Please verify stock.");
                }
            }
        } catch (IOException e) {
            System.out.println("File not found or access error.");
        } catch (NumberFormatException e) {
            System.out.println("Error: The CSV file contains non-numeric stock data.");
        }
    }

    // 2. Stock transfer
    private static void stockTransfer(Scanner sc, CurrentEmployee current){
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        String transType = "";

        //Option for Stock in Stock Out
        while (true) {
            System.out.println("Stock In or Stock Out?");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Stock In") || input.equalsIgnoreCase("In")) {
                transType = "Stock In";
                break;
            } else if (input.equalsIgnoreCase("Stock Out") || input.equalsIgnoreCase("Out")) {
                transType = "Stock Out";
                break;
            } else {
                System.out.println("Invalid option. Please type 'Stock In' or 'Stock Out'.");
            }
        }
        
        //Details
        System.out.println("");
        System.out.println("=== " + transType + " ===");
        System.out.println("Date: " + date.format(dateForm));
        System.out.println("Time: " + time.format(timeForm)); 
        System.out.print("From: "); 
        String fromOutlet = sc.nextLine().trim();
        System.out.print("To: ");
        String toOutlet = sc.nextLine().trim();

        List<String> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        int totalQuantity = 0;

        while (true) {
            System.out.print("Enter Product ID (Type 'STOP' when done): ");
            String product_id = sc.nextLine().trim();
            if (product_id.equalsIgnoreCase("STOP")) break;

            System.out.print("Quantity for " + product_id + ": ");
            int quantity = 0;
            
            try {
                quantity = Integer.parseInt(sc.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number for quantity. Try again.");
                continue;
            }

            // Update CSV file
            if (updateInventoryFile(product_id, fromOutlet, toOutlet, quantity)) {
                products.add(product_id);
                quantities.add(quantity);
                totalQuantity += quantity;
                System.out.println("Model quantities updated successfully.");
                System.out.println(transType+" recorded.");
                
            } else {
                System.out.println("Failed to update " + product_id + ". Check ID, Outlet Names, or Stock Level.");
            }
        }
        
        // trivial case
        if (products.isEmpty()) {
            System.out.println("No items processed. Exiting.");
            return;
        }

        System.out.println("Employee ID: " + current.getEmployeeID());
        String employee_id = current.getEmployeeID();

        //Generate receipt
        String fileName = generateReceipt(date, time, timeForm, transType, fromOutlet, toOutlet, products, quantities, totalQuantity, employee_id);

        if (fileName != null) {
            System.out.println("Receipt generated: " + fileName);
        }
    }

    // 2. Update the csv file
    private static boolean updateInventoryFile(String product_id, String fromOutlet, String toOutlet, int quantity) {
        List<String> lines = new ArrayList<>();
        boolean IDFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader("Inventory.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            return false;
        }

        if (lines.isEmpty()) return false;

        String[] headers = lines.get(0).split(",");
        int fromIndex = -1;
        int toIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(fromOutlet)) fromIndex = i;
            if (headers[i].trim().equalsIgnoreCase(toOutlet)) toIndex = i;
        }
        
        if (fromIndex == -1 && toIndex == -1) {
            System.out.println("Error: Neither 'From' nor 'To' outlets matched CSV headers.");
            return false;
        }

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length > 0 && parts[0].trim().equalsIgnoreCase(product_id)) {
                IDFound = true;
                
                if (fromIndex != -1) {
                    try {
                        int stock = Integer.parseInt(parts[fromIndex].trim());
                        if (stock < quantity) {
                            System.out.println("Error: Insufficient stock in " + fromOutlet + " (Current: " + stock + ")");
                            return false;
                        }
                        parts[fromIndex] = String.valueOf(stock - quantity);
                    } catch (NumberFormatException e) {
                        return false; 
                    }
                }
                
                if (toIndex != -1) {
                    try {
                        int stock = Integer.parseInt(parts[toIndex].trim());
                        parts[toIndex] = String.valueOf(stock + quantity);
                    } catch (NumberFormatException e) {
                        parts[toIndex] = String.valueOf(quantity);
                    }
                }
                lines.set(i, String.join(",", parts));
                break;
            }
        }

        if (!IDFound) return false;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Inventory.csv"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // 3. Generate receipt
    private static String generateReceipt(LocalDate date, LocalTime time, DateTimeFormatter timeForm,
                                        String type, String fromOutlet, String toOutlet, 
                                        List<String> products, List<Integer> quantities, int totalQuantity, String employee_id) {
        
        String fileName = "receipts_" + date.toString() + ".txt";
        
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            out.println("=== " + type + " ===");
            out.println("Date: " + date);
            out.println("Time: " + time.format(timeForm));
            out.println("From: " + fromOutlet);
            out.println("To: " + toOutlet);
            out.print("Models: ");
            for (int i = 0; i < products.size(); i++) {
                out.print("- " + products.get(i) + " (Quantity: " + quantities.get(i) + ") ");
            }
            out.println();
            out.println("Total Quantity: " + totalQuantity);
            out.println("Employee ID: " + employee_id);
            out.println("------------------------------------------------");
        
            return fileName;
            
        } catch (IOException e) {
            System.out.println("Receipt failed to generate.");
            return null; 
        }
    }
}
