import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
 

public class Receipt {
    public static void daily_sales_report(String date){
        if (!SearchInformation.is_date(date)) {System.out.println("Invalid date! "); return;}

        String file_name = "sales_"+date+".txt";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("name", "");
        ArrayList<Transaction> transaction = SearchInformation.search_sales_information(params);     // transaction filtered by date


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_name))){
            if (transaction.isEmpty()){writer.append("No transactions on "+date); return;}

            int last_id = -1;
            double daily_total = 0;
            double total = 0;
            int num = 0;
            writer.append("=== Transactions on "+date+" ===\n");
            for (Transaction t : transaction){
                if (t.get_transaction_id() != last_id){
                    if (total!=0) {writer.append("\n\nGrand Total: "+total+"\n");}
                    total = 0;    // reinitialise total = 0
                    num += 1;

                    // new transaction header
                    writer.append("\n=== Transaction ID: "+t.get_transaction_id()+" ===\n");
                    writer.append("\nEmployee ID: "+t.get_employee_id());
                    writer.append("\nDate: "+t.get_date());
                    writer.append("\nTime: "+t.get_time());
                    writer.append("\nCustomer Name: "+t.get_customer_name());
                    writer.append("\nPayment Method: "+t.get_payment_method());
                    writer.append("\nOutlet: "+t.get_outlet());

                    // 1st item
                    writer.append("\nModel: "+t.get_product_id());
                    writer.append("\nQuantity: "+t.get_quantity());
                    writer.append("\nTotal: "+t.get_total());
                }
                // other items in the same transaction
                else{
                    writer.append("\nModel: "+t.get_product_id());
                    writer.append("\nQuantity: "+t.get_quantity());
                    writer.append("\nTotal: "+t.get_total());
                }
                last_id = t.get_transaction_id();
                total += t.get_total();
                daily_total += t.get_total();
            }
            if (total != 0) {writer.append("\n\nGrand Total: " + total+"\n");}
            writer.append("\n=== Summary ===\n");
            writer.append("\nNumber of transactions: "+num);
            writer.append("\nTotal revenue: "+daily_total);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        daily_sales_report("2025-12-02");
        daily_sales_report("2025-12-24");
        daily_sales_report("2025-12-12");
    }
}
