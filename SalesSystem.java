import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class SalesSystem {

    public void recordSale(Scanner input, CurrentEmployee current) {
        ArrayList<Transaction> transaction = Transaction.readCsv();
        ArrayList<Inventory> inventory = Inventory.readCsv();
        final int current_transaction_id = transaction.getLast().get_transaction_id()+1;

        String employee_id = current.getEmployeeID();
        String location = current.getOutlet();

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

        String dateStr = date.format(df);
        String timeStr = time.format(tf);

        System.out.println("=== Record New Sale ===");
        System.out.println("Date: " + dateStr);
        System.out.println("Time: " + timeStr);

        System.out.print("\nCustomer Name: ");
        String customer = input.nextLine();

        double subtotal = 0;
        ArrayList<Transaction> new_transaction = new ArrayList<>();

        // Start transaction
        while (true) {
            System.out.print("\nEnter Model: ");
            String productID = input.nextLine();

            System.out.print("Enter Quantity: ");
            int qty = input.nextInt();
            input.nextLine();

            double price = getPrice(productID);
            double total = price * qty;
            subtotal += total;

            Transaction t = new Transaction(
                current_transaction_id, 
                employee_id,      
                dateStr, 
                timeStr, 
                customer, 
                productID, 
                qty,
                "",        
                location,        
                total);
            
            // Store temporary new transactions
            new_transaction.add(t);
            System.out.println("Unit Price: RM" + price);

            // Update inventory
            for (int i=0; i<inventory.size(); i++){
                if (inventory.get(i).get_product_id().equals(productID)){
                    if (current.getOutlet().equals("KLCC")) {inventory.get(i).set_stock_klcc(inventory.get(i).get_stock_klcc()-qty);}
                    else {inventory.get(i).set_stock_lot10(inventory.get(i).get_stock_lot10()-qty);}
                }
            } 

            System.out.print("Are there more items purchased? (Y/N): ");
            String c = input.nextLine();
            if (c.equalsIgnoreCase("N")) {break;}
        }

        System.out.print("\nEnter transaction method: ");
        String method = input.nextLine();

        // Set all payment methods & append to transaction
        for (int i=0; i<new_transaction.size(); i++){
            new_transaction.get(i).set_payment_method(method);
            transaction.add(new_transaction.get(i));
        }

        // Write to csv 
        Transaction.writeCsv(transaction);
        Inventory.writeCsv(inventory);

        System.out.println("Subtotal: RM" + String.format("%.2f", subtotal));
        generateReceipt(current_transaction_id, dateStr, timeStr, customer, subtotal, method);

        System.out.println("\nTransaction successful.");
        System.out.println("Sale recorded successfully.");
        System.out.println("Receipt generated: sales_" + dateStr.replace("/", "-") + ".txt");
    }

    private double getPrice(String productID) {
        ArrayList<Product> product = Product.readCsv();
        for (Product p : product){
            if (p.get_product_id().equalsIgnoreCase(productID)) {return p.get_selling_price();}
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

