import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedHashMap;

public class EditInformation {
    public static void main_menu(Scanner sc){
        int choice;
        System.out.println("=== Edit Information ===");
        System.out.println("1. Edit Stock Information");
        System.out.println("2. Edit Sales Information");
        System.out.println("Enter 0 to ESC");
        while (true){
            choice = sc.nextInt();
            if (choice != 0 && choice != 1 && choice != 2) {System.out.println("Invalid input! "); continue;}
            break;
        }
        switch (choice){
            case 0:
                System.out.println("Edit information cancelled");
                return;
            case 1:
                edit_stock_information(sc);
                break;
            case 2:
                edit_sales_information(sc);
                break;
        }
    }

    private static void edit_stock_information(Scanner sc){
        ArrayList<Inventory> inventory = Inventory.readCsv();
        System.out.println("=== Edit Stock Information ===\n");

        // 1. input product_id
        System.out.println("Enter product ID: ");
        String product_id = sc.nextLine();
        if (product_id.equals("")) {return;}

        boolean found = false;
        int idx = 0;
        for (Inventory i : inventory){
            if (i.get_product_id().equals(product_id)){
                found = true;
                break;
            }
            idx++;
        }
        if (!found) {
            System.out.println("Product not found! ");
            return;
        }
        else{
            int outlet;
            while (true){
                // 2. input outlet
                System.out.println("\nEnter outlet of stock information to edit: ");
                System.out.println("1. Lot 10     2. KLCC");
                System.out.println("Enter 0 to ESC ");
                outlet = sc.nextInt();
                // 3. input new quantity
                if (outlet==0 || outlet==1 || outlet==2){
                    switch (outlet){
                        case 1:
                            System.out.println("\nNew stock quantity: ");
                            inventory.get(idx).set_stock_lot10(sc.nextInt());
                            break;
                        case 2:
                            System.out.println("\nNew stock quantity: ");
                            inventory.get(idx).set_stock_klcc(sc.nextInt());
                            break;
                        case 0:
                            System.out.println("\nEdit information cancelled");
                            return;
                    }
                    break;
                }
                else {System.out.println("Enter either 1, 2, or 0! ");}
            }
        }

        // 4. write in Inventory.csv
        Inventory.writeCsv(inventory);
        System.out.println("Changes have been made ");
        return;
    }

    private static void edit_sales_information(Scanner sc){
        ArrayList<Transaction> transaction = Transaction.readCsv();
        ArrayList<Product> products = Product.readCsv();
        ArrayList<Inventory> inventory = Inventory.readCsv();

        // 1. filter by transaction_date & customer_name (validate)
        System.out.println("=== Edit Sales Information ===\n");
        String date;
        while (true){
            System.out.println("\nTransaction date: ");
            date = sc.nextLine();
            if (!SearchInformation.is_date(date)) {System.out.println("Invalid date! "); continue;}
            break;
        }
        System.out.println("Customer name: ");
        String customer_name = sc.nextLine();

        // 2. find matching transaction(s)
        LinkedHashMap<String, String> filter = new LinkedHashMap<>();
        filter.put("date", date);
        filter.put("name", customer_name);

        ArrayList<Transaction> found = SearchInformation.search_sales_information(filter);
        SearchInformation.render_searched_sales(found);

        // 3. pick transaction
        int choice_transaction;
        if (found.size() < 1) {return;}   // return if no transactions found
        else{
            if (found.size() > 1) {
                System.out.println("Select transaction number to continue (1 to "+found.size()+"): ");
                choice_transaction = sc.nextInt();
            }
            else {choice_transaction = 1;}
        }
        int idx = choice_transaction-1;

        Transaction in_useTransaction = found.get(idx);
        int in_useTransactionId = in_useTransaction.get_transaction_id();

        // 4. select to edit 
        int choice_edit;
        while (true){
            System.out.println("\nSelect number to edit: ");
            System.out.println("1. Name     2. Model     3. Quantity     4. Payment method");
            System.out.println("Enter 0 to ESC ");

            choice_edit = sc.nextInt();
            if (choice_edit < 0 || choice_edit > 4){
                System.out.println("Invalid choice! ");
                continue;     
            }
            else{
                switch (choice_edit){
                    case 0:
                        System.out.println("Edit information cancelled! ");
                        return;
                    case 1:
                        sc.nextLine();
                        System.out.println("\nEnter new customer name: ");
                        System.out.println("Enter 0 to ESC ");
                        String customer_name_edit = sc.nextLine();

                        if (customer_name_edit.equals("0")){System.out.println("Edit information cancelled! "); return;}
                        for (int i=0; i<transaction.size(); i++){
                            if (transaction.get(i).get_transaction_id() == in_useTransactionId){
                                transaction.get(i).set_customer_name(customer_name_edit);
                            }
                        }
                        break;

                    case 2:
                        sc.nextLine();
                        System.out.println("\nEnter new model name: ");
                        System.out.println("Enter 0 to ESC ");
                        int quantity = in_useTransaction.get_quantity();
                        String product_id_before = in_useTransaction.get_product_id();
                        String product_id_edit = sc.nextLine();

                        if (product_id_edit.equals("0")){System.out.println("Edit information cancelled! "); return;}
                        in_useTransaction.set_product_id(product_id_edit);

                        // change total
                        for (Product p : products){
                            if (p.get_product_id().equals(product_id_edit)){
                                in_useTransaction.set_total(in_useTransaction.get_quantity()*p.get_selling_price());   // quantity * selling_price
                            }
                        }

                        // change inventory
                        for (int i=0; i<inventory.size(); i++){
                            // refund (+)
                            if (inventory.get(i).get_product_id().equals(product_id_before)){
                                if (in_useTransaction.get_outlet().equals("KLCC")) {
                                    inventory.get(i).set_stock_klcc(inventory.get(i).get_stock_klcc()+quantity);
                                }
                                if (in_useTransaction.get_outlet().equals("Lot_10")) {
                                    inventory.get(i).set_stock_lot10(inventory.get(i).get_stock_lot10()+quantity);
                                }
                            }

                            // sell (-)
                            if (inventory.get(i).get_product_id().equals(product_id_edit)){
                                if (in_useTransaction.get_outlet().equals("KLCC")) {
                                    inventory.get(i).set_stock_klcc(inventory.get(i).get_stock_klcc()-quantity);
                                }
                                if (in_useTransaction.get_outlet().equals("Lot_10")) {
                                    inventory.get(i).set_stock_lot10(inventory.get(i).get_stock_lot10()-quantity);
                                }
                            }
                        } 
                        break;

                    case 3: 
                        sc.nextLine();
                        int quantity_before = in_useTransaction.get_quantity();
                        int quantity_now;
                        while (true){
                            System.out.println("\nEnter new quantity: ");
                            System.out.println("Enter 0 to ESC ");
                            quantity_now = Integer.parseInt(sc.nextLine());
                            if (quantity_now < 0) {System.out.println("Invalid quantity! "); continue;}
                            break;
                        }
                        if (quantity_now == 0){System.out.println("Edit information cancelled! "); return;}
                        in_useTransaction.set_quantity(quantity_now);

                        // change total
                        for (Product p : products){
                            if (p.get_product_id().equals(in_useTransaction.get_product_id())){
                                in_useTransaction.set_total(quantity_now*p.get_selling_price());    
                            }
                        }

                        // change inventory
                        for (int i=0; i<inventory.size(); i++){
                            if (inventory.get(i).get_product_id().equals(in_useTransaction.get_product_id())){
                                if (in_useTransaction.get_outlet().equals("KLCC")) {
                                    inventory.get(i).set_stock_klcc(inventory.get(i).get_stock_klcc()+quantity_before-quantity_now);
                                }
                                if (in_useTransaction.get_outlet().equals("Lot_10")) {
                                    inventory.get(i).set_stock_lot10(inventory.get(i).get_stock_lot10()+quantity_before-quantity_now);
                                }
                            }
                        } 
                        break;

                    case 4:
                        sc.nextLine();
                        int payment_method_edit_choice;
                        System.out.println("\nSelect payment method: ");
                        System.out.println("1. Credit Card     2. Debit Card     3. Cash     4. E-wallet ");
                        System.out.println("Enter 0 to ESC ");
                        payment_method_edit_choice = Integer.parseInt(sc.nextLine());   
                        String payment_method="";

                        if (payment_method_edit_choice == 0){System.out.println("Edit information cancelled! "); return;}
                        switch (payment_method_edit_choice){
                            case 1: 
                                payment_method = "Credit Card";
                                break;
                            case 2:
                                payment_method = "Debit Card";
                                break;
                            case 3:
                                payment_method = "Cash";
                                break;
                            case 4:
                                payment_method = "E-wallet";
                                break;
                        }
                        for (int i=0; i<transaction.size(); i++){
                            if (transaction.get(i).get_transaction_id() == in_useTransactionId){
                                transaction.get(i).set_payment_method(payment_method);
                            }
                        }
                        break;
    
                }
                break;
            }
        }

        // 5. insert altered transaction data + update Inventory.csv
        if (choice_edit == 2 || choice_edit == 3){
            for (int i=0; i<transaction.size(); i++){
                if (transaction.get(i).get_transaction_id() == in_useTransactionId) {
                    transaction.set(i, in_useTransaction); 
                    break;
                }
            }
            Inventory.writeCsv(inventory);
            System.out.println("Changes to inventory has been made");
        }


        // 6. update Transaction.csv
        Transaction.writeCsv(transaction);
        System.out.println("Changes to transaction has been made");
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        edit_sales_information(sc);
        sc.close();
    }

}
