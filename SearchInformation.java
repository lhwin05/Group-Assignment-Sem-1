import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedHashMap;

public class SearchInformation {
    public static void main_menu(Scanner sc){
        int choice;
        System.out.println("=== Search Sales Information ===");
        System.out.println("1. Search Stock Information");
        System.out.println("2. Search Sales Information");
        System.out.println("Enter 0 to ESC\n");
        while (true){
            choice = sc.nextInt();
            if (choice != 0 && choice != 1 && choice != 2) {System.out.println("Invalid input! "); continue;}
            break;
        }
        switch (choice){
            case 0:
                System.out.println("Search information cancelled");
                return;
            case 1:
                sc.nextLine();
                search_by_stock_interface(sc);
                break;
            case 2:
                sc.nextLine();
                search_sales_information_interface(sc);
                break;
        }
    }

    private static void search_by_stock_interface(Scanner sc){
        System.out.println("=== Search Stock Information ===\n");
        String product_id;
        while (true) {
            System.out.println("\nEnter product ID to search: ");
            sc. nextLine();
            product_id = sc.nextLine();
            if (!product_id.equals("")) {break;} 
        }
        search_by_stock(product_id);
    }

    private static void search_by_stock(String product_id){
        ArrayList<Inventory> inventory = new ArrayList<>();
        inventory = Inventory.readCsv();
        boolean found = false;

        for (Inventory inv : inventory){
            if (inv.get_product_id().equals(product_id)){
                System.out.println("\nModel: " + inv.get_product_id());
                System.out.println("Lot 10 stock: " + inv.get_stock_lot10());
                System.out.println("KLCC stock: " + inv.get_stock_klcc());
                found = true;
                break;
            }
        }
        if (!found) {System.out.println("Invalid product_id! ");}
    }

    // validate date in format yyyy-mm-dd
    public static boolean is_date(String date){
        if (date.length()==10 && date.substring(4,5).equals("-") && date.substring(7,8).equals("-")){
            return true;
        }
        return false;
    }

    public static void search_sales_information_interface(Scanner sc){
        LinkedHashMap<String, String> filter = new LinkedHashMap<>();
        System.out.println("=== Search Sales Information ===\n");
        String date;
        while (true){
            System.out.println("Sales date:     (press Enter to disregard filter)");
            date = sc.nextLine();
            if (date.equals("") || is_date(date)) {break;}
        }
        filter.put("date", date);

        System.out.println("\nCustomer name:     (press Enter to disregard filter)");
        String name = sc.nextLine();
        filter.put("name", name);
        
        ArrayList<Transaction> to_display = search_sales_information(filter);
        render_searched_sales(to_display);
    } 

    public static ArrayList<Transaction> search_sales_information(LinkedHashMap<String, String> filter){
        String date_filter = filter.get("date");
        String name_filter = filter.get("name");

        // trivial case: no filter
        ArrayList<Transaction> filter_0 = new ArrayList<>();
        filter_0 = Transaction.readCsv();
        
        if ("".equals(date_filter) && "".equals(name_filter)) {return filter_0;}

        // filter by date --> name
        ArrayList<Transaction> filter_1 = new ArrayList<>();
        if (!"".equals(date_filter)){
            for (Transaction t : filter_0){
                if (t.get_date().equals(date_filter)) {filter_1.add(t);}
            }
        } 
        else {filter_1 = filter_0;}

        ArrayList<Transaction> filter_2 = new ArrayList<>();
        if (!"".equals(name_filter)) {
            for (Transaction t : filter_1){
                if (t.get_customer_name().equals(name_filter)) {filter_2.add(t);}
            }
        }
        else {filter_2 = filter_1;}

        return filter_2;
    }

    public static void render_searched_sales(ArrayList<Transaction> transaction){
        if (!transaction.isEmpty()){
            System.out.println("Sales Record Found: ");
            int i = 1;
            for (Transaction t : transaction){
                System.out.println();
                System.out.println(i+".");
                System.out.println("Transaction ID: " + t.get_transaction_id());
                System.out.println("Employee ID: " + t.get_employee_id());
                System.out.println("Date: " + t.get_date());
                System.out.println("Time: " + t.get_time());
                System.out.println("Payment method: " + t.get_payment_method());
                System.out.println("Outlet location: " + t.get_outlet());
                i++;
            }
        } 
        else {
            System.out.println("No Record Found! ");
        }
    }

    public static void main(String[] args){
    }

    
}
