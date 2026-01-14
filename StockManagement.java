import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockManagement {

    
    public static void main_menu(Scanner sc, CurrentEmployee current) {
        
        
        while (true) {
            System.out.println("");
            System.out.println("\n=== Stock Management ===");
            System.out.println("1. Stock Transfer");
            System.out.println("2. Stock Count");
            System.out.println("3. Back to Main Menu"); // Added exit option

            int choice;
            while (true) {
                try {
                    System.out.print("Enter choice: ");
                    // Fixed: Use parsed line to prevent skipping inputs
                    choice = Integer.parseInt(sc.nextLine().trim());
                    if (choice < 1 || choice > 3) {
                        System.out.println("Invalid input! Enter 1, 2, or 3.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            switch (choice) {
                case 1:
                    stockTransfer(sc, current);
                    break;
                case 2:
                    stockCount(sc);
                    break;
                case 3:
                    return; // This returns the user to your "Record New Sale" / Main Menu
            }
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

        String outlet = "";
        while (true) {
            System.out.println("\nOutlet Location:");
            System.out.println("1. KLCC     2. Lot_10");
            try {
                int outlet_choice = Integer.parseInt(sc.nextLine().trim());
                if (outlet_choice == 1) {
                    outlet = "KLCC";
                    break;
                } else if (outlet_choice == 2) {
                    outlet = "Lot_10";
                    break;
                } else {
                    System.out.println("Invalid input!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }

        String countType = "";
        while (true) {
            System.out.println("Morning OR Night Stock Count?");
            countType = sc.nextLine().trim();

            if (countType.equalsIgnoreCase("Morning")) {
                countType = "Morning";
                break;
            } else if (countType.equalsIgnoreCase("Night")) {
                countType = "Night";
                break;
            } else {
                System.out.println("Try again (Type 'Morning' or 'Night').");
            }
        }

        System.out.println("\n=== " + countType + " Stock Count ===");
        System.out.println("Date: " + date.format(dateForm));
        System.out.println("Time: " + time.format(timeForm));

        File file = new File("Inventory.csv");
        if (!file.exists()) {
            System.out.println("Inventory.csv not found!");
            return;
        }

        try (Scanner ScanInventory = new Scanner(file)) {
            if (!ScanInventory.hasNextLine()) return;

            
            String headerLine = ScanInventory.nextLine();
            String[] headers = headerLine.split(",");
            int outletIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(outlet)) {
                    outletIndex = i;
                    break;
                }
            }

            if (outletIndex == -1) {
                System.out.println("Error: Outlet '" + outlet + "' not found in CSV headers.");
                return;
            }

            while (ScanInventory.hasNextLine()) {
                String line = ScanInventory.nextLine();
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] parts = line.split(",");
                if (parts.length <= outletIndex) continue; // Skip malformed lines

                String id = parts[0];
                int stockRecord = 0;
                
                try {
                    stockRecord = Integer.parseInt(parts[outletIndex].trim());
                } catch (NumberFormatException e) {
                    continue; // Skip lines with bad number data
                }

                System.out.print("Model: " + id + " -- Counted: ");
                int counted = 0;
                
                try {
                    String input = sc.nextLine().trim();
                    if (input.equalsIgnoreCase("stop")) break;
                    counted = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, skipping item.");
                    continue;
                }

                if (counted == stockRecord) {
                    System.out.println("Store Record: " + stockRecord);
                    System.out.println("Status: Correct\n");
                    totalTally++;
                } else {
                    System.out.println("Store Record: " + stockRecord);
                    int diff = Math.abs(counted - stockRecord);
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
            
            // Wait for user before returning to menu
            System.out.println("Press Enter to return to menu...");
            sc.nextLine();

        } catch (IOException e) {
            System.out.println("File error.");
        }
    }

    // 2. Stock transfer
    private static void stockTransfer(Scanner sc, CurrentEmployee current) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        String transType = "";

        while (true) {
            System.out.println("Stock In (In) or Stock Out (Out)? Type 'Back' to cancel.");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Stock In") || input.equalsIgnoreCase("In")) {
                transType = "Stock In";
                break;
            } else if (input.equalsIgnoreCase("Stock Out") || input.equalsIgnoreCase("Out")) {
                transType = "Stock Out";
                break;
            } else if (input.equalsIgnoreCase("Back")) {
                return;
            } else {
                System.out.println("Invalid option.");
            }
        }
        
        System.out.println("\n=== " + transType + " ===");
        System.out.print("From (Outlet Name): "); 
        String fromOutlet = sc.nextLine().trim();
        System.out.print("To (Outlet Name): ");
        String toOutlet = sc.nextLine().trim();

        List<String> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        int totalQuantity = 0;

        while (true) {
            System.out.print("Enter Product ID (Type 'STOP' when done): ");
            String product_id = sc.nextLine().trim();
            if (product_id.equalsIgnoreCase("STOP")) break;
            if (product_id.isEmpty()) continue;

            System.out.print("Quantity for " + product_id + ": ");
            int quantity = 0;
            
            try {
                quantity = Integer.parseInt(sc.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
                continue;
            }

            if (updateInventoryFile(product_id, fromOutlet, toOutlet, quantity)) {
                products.add(product_id);
                quantities.add(quantity);
                totalQuantity += quantity;
                System.out.println("Success.");
            } else {
                System.out.println("Failed to update " + product_id + ". Check ID or Outlet names.");
            }
        }
        
        if (products.isEmpty()) {
            System.out.println("No items processed.");
            return;
        }

        String employee_id = current.getEmployeeID();

        String fileName = generateReceipt(date, time, timeForm, transType, fromOutlet, toOutlet, products, quantities, totalQuantity, employee_id);

        if (fileName != null) {
            System.out.println("Receipt generated: " + fileName);
        }
        System.out.println("Press Enter to return to menu...");
        sc.nextLine();
    }

    // Update CSV Helper
    private static boolean updateInventoryFile(String product_id, String fromOutlet, String toOutlet, int quantity) {
        List<String> lines = new ArrayList<>();
        boolean IDFound = false;

        File file = new File("Inventory.csv");
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) { return false; }

        if (lines.isEmpty()) return false;

        String[] headers = lines.get(0).split(",");
        int fromIndex = -1;
        int toIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(fromOutlet)) fromIndex = i;
            if (headers[i].trim().equalsIgnoreCase(toOutlet)) toIndex = i;
        }
        
        if (fromIndex == -1 && toIndex == -1) {
            System.out.println("Error: Outlets not found in CSV header.");
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
                            System.out.println("Insufficient stock in " + fromOutlet);
                            return false;
                        }
                        parts[fromIndex] = String.valueOf(stock - quantity);
                    } catch (Exception e) { return false; }
                }
                
                if (toIndex != -1) {
                    try {
                        int stock = Integer.parseInt(parts[toIndex].trim());
                        parts[toIndex] = String.valueOf(stock + quantity);
                    } catch (Exception e) { 
                        parts[toIndex] = String.valueOf(quantity); 
                    }
                }
                lines.set(i, String.join(",", parts));
                break;
            }
        }

        if (!IDFound) return false;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) { return false; }
        return true;
    }

    // Receipt Generator
    private static String generateReceipt(LocalDate date, LocalTime time, DateTimeFormatter timeForm,
                                        String type, String fromOutlet, String toOutlet, 
                                        List<String> products, List<Integer> quantities, int totalQuantity, String employee_id) {
        
        String fileName = "receipts_" + date.toString() + ".txt";
        
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            out.println("=== " + type + " ===");
            out.println("Date: " + date + " " + time.format(timeForm));
            out.println("From: " + fromOutlet + " -> To: " + toOutlet);
            out.println("Employee: " + employee_id);
            out.println("--------------------------------");
            for (int i = 0; i < products.size(); i++) {
                out.println("- " + products.get(i) + " (Qty: " + quantities.get(i) + ")");
            }
            out.println("--------------------------------");
            out.println("Total Qty: " + totalQuantity);
            out.println("================================");
            out.println(); // Empty line
            return fileName;
        } catch (IOException e) {
            System.out.println("Receipt failed.");
            return null; 
        }
    }
}
