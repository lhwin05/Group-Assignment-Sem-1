import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class stockCount {

    public static void main(String[] args) {

        // DateAndTime
        LocalTime time = LocalTime.now();
        DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int totalMiss = 0;
        int totalTally = 0;
        int totalCheck = 0;

        // Try-with-resources handles automatic closing of the Scanner
        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("Outlet Location:");
            String outlet = sc.nextLine();
            String countType = "";

            while (true) {
                System.out.println("Morning OR Night Stock Count");
                countType = sc.nextLine();

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

            System.out.println("=== " + countType + " Stock Count ===");
            System.out.println("Date: " + date.format(dateForm));
            System.out.println("Time: " + time.format(timeForm));

            File file = new File("Inventory.csv");
            try (Scanner ScanInventory = new Scanner(file)) {
                if (ScanInventory.hasNextLine()) {
                    ScanInventory.nextLine(); // Skip header
                }

                while (ScanInventory.hasNextLine()) {
                    String line = ScanInventory.nextLine();
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
                        // If location is wrong, we shouldn't process the file
                        System.out.println("Invalid Location.");
                        return; 
                    }

                    System.out.print("Model: " + id + " -- Counted: ");
                    
                    // Optimization: Use sc.nextLine() + Integer.parseInt to avoid 
                    // the common "Scanner skip" bug caused by sc.nextInt()
                    int counted = Integer.parseInt(sc.nextLine());

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
}