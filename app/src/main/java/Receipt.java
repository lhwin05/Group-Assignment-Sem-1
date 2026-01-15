import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
 

public class Receipt {
    public static String daily_sales_report(String date){
        // Validate date
        if (!SearchInformation.is_date(date)) {System.out.println("Invalid date! "); return "";}

        // Filter transaction by date
        String file_name = "sales_"+date+".txt";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("name", "");
        ArrayList<Transaction> transaction = SearchInformation.search_sales_information(params);    

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_name))){
            if (transaction.isEmpty()){writer.append("No transactions on "+date); return "";}

            int last_id = -1;
            double daily_total = 0;
            double total = 0;
            int num = 0;
            writer.append("=== Transactions on "+date+" ===\n");
            for (Transaction t : transaction){
                // new transaction
                if (t.get_transaction_id() != last_id){
                    if (total!=0) {writer.append("\n\nGrand Total: "+total+"\n");}
                    total = 0;    
                    num += 1;      // number of transactions

                    // transaction header
                    writer.append("\n=== Transaction ID: "+t.get_transaction_id()+" ===\n");
                    writer.append("\nEmployee ID: "+t.get_employee_id());
                    writer.append("\nDate: "+t.get_date());
                    writer.append("\nTime: "+t.get_time());
                    writer.append("\nCustomer Name: "+t.get_customer_name());
                    writer.append("\nPayment Method: "+t.get_payment_method());
                    writer.append("\nOutlet: "+t.get_outlet());
                }
                // items in the same transaction
                writer.append("\nModel: "+t.get_product_id());
                writer.append("\nQuantity: "+t.get_quantity());
                writer.append("\nTotal: "+t.get_total());
                last_id = t.get_transaction_id();
                total += t.get_total();
                daily_total += t.get_total();
            }
            if (total != 0) {writer.append("\n\nGrand Total: " + total+"\n");}
            writer.append("\n=== Summary ===\n");
            writer.append("\nNumber of transactions: "+num);
            writer.append("\nTotal revenue: "+daily_total);

            return file_name;
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        
    }
}
