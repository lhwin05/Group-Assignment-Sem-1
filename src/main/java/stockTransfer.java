import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class stockTransfer {

    public static void main(String[] args) {
        
        //Date&Time
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        Scanner sc = new Scanner(System.in);
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
            //Prevent program from crashing when quantity is invalid.
            try {
                quantity = sc.nextInt();
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    sc.nextLine();
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid number for quantity. Try again.");
                sc.nextLine();
                continue;
            }
            sc.nextLine(); 

            //Update CSV file
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
        
        if (products.isEmpty()) {
            System.out.println("No items processed. Exiting.");
            return;
        }

        System.out.println("Employee ID: ");
        String employee_id = sc.nextLine();

        //Generate receipt
        String fileName = generateReceipt(date, time, timeForm, transType, fromOutlet, toOutlet, products, quantities, totalQuantity, employee_id);

            if (fileName != null) {
                System.out.println("Receipt generated: " + fileName);
            }
        
        sc.close();
    }

    //Method to update the csv file
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
                        //If destination cell is empty or invalid, assume 0 start
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

    //Method to generate receipt
    
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
        
        return fileName; //Return the filename receipt is generated
        
    } catch (IOException e) {
        System.out.println("Receipt failed to generate.");
        return null; //Return null when receipt is not generated
    }
    }
}