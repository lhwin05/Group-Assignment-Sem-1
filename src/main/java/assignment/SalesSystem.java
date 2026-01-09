package assignment;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SalesSystem {

    private static final String PRODUCT_FILE = "Products.csv";
    private TransactionWriter writer = new TransactionWriter();

    public void recordSale(Scanner input) {

        TransactionFileReader idReader = new TransactionFileReader();
        int transactionID = idReader.getNextTransactionID();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

        String dateStr = date.format(df);
        String timeStr = time.format(tf);

        System.out.println("=== Record New Sale ===");
        System.out.println("Date: " + dateStr);
        System.out.println("Time: " + timeStr);

        System.out.print("\nCustomer Name: ");
        String customer = input.nextLine();

        double subtotal = 0;
        boolean more = true;

        while (more) {

            System.out.print("\nEnter Model: ");
            String productID = input.nextLine();

            System.out.print("Enter Quantity: ");
            int qty = input.nextInt();
            input.nextLine();

            double price = getPrice(productID);
            double total = price * qty;
            subtotal += total;

            Transaction t = new Transaction(transactionID, "c1001",      // later replace with current IDlogin
                    dateStr, timeStr, customer, productID, qty,
                    "KLCC",         // later replace with current login
                    total);

            writer.save(t);

            System.out.println("Unit Price: RM" + price);

            System.out.print("Are there more items purchased? (Y/N): ");
            char c = input.nextLine().charAt(0);
            if (c == 'N' || c == 'n') {
                more = false;
            }
        }

        System.out.print("\nEnter transaction method: ");
        String method = input.nextLine();

        System.out.println("Subtotal: RM" + String.format("%.2f", subtotal));
        generateReceipt(transactionID, dateStr, timeStr, customer, subtotal, method);

        System.out.println("\nTransaction successful.");
        System.out.println("Sale recorded successfully.");
        System.out.println("Receipt generated: sales_" + dateStr.replace("/", "-") + ".txt");
    }


    private double getPrice(String productID) {
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equalsIgnoreCase(productID)) {
                    return Double.parseDouble(d[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading product file.");
        }
        return 0;
    }

    private void generateReceipt(int id, String date, String time, String customer, double total, String method) {

        String safedate = date.replace("/","-");
        String file = "sales_" + safedate + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println("=================================");
            pw.println("         SALES RECEIPT            ");
            pw.println("=================================");
            pw.println("Transaction ID : " + id);
            pw.println("Date           : " + date);
            pw.println("Time           : " + time);
            pw.println("Customer       : " + customer);
            pw.println("Payment Method : " + method);
            pw.printf ("Total (RM)     : %.2f\n", total);
            pw.println("=================================\n");
        } catch (IOException e) {
            System.out.println("Error generating receipt.");
        }
    }
}

