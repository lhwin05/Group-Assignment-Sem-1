import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Transaction {
    private int transaction_id;
    private String employee_id;
    private String transaction_date;
    private String transaction_time;
    private String customer_name;
    private String product_id;
    private int quantity;
    private String payment_method;
    private String outlet;
    private double total;

    public Transaction(int t_id, String e_id, String date, String time, String name, String p_id, int quant, String pm, String loc, double tot){
        transaction_id = t_id;
        employee_id = e_id;
        transaction_date = date;
        transaction_time = time;
        customer_name = name;
        product_id = p_id;
        quantity = quant; 
        payment_method = pm;
        outlet = loc;
        total = tot;
    }

    public int get_transaction_id(){return transaction_id;}
    public String get_employee_id(){return employee_id;}
    public String get_date(){return transaction_date;}
    public String get_time(){return transaction_time;}
    public String get_customer_name(){return customer_name;}
    public String get_product_id(){return product_id;}
    public int get_quantity(){return quantity;}
    public String get_payment_method(){return payment_method;}
    public String get_outlet(){return outlet;}
    public double get_total(){return total;}

    public void set_customer_name(String c){customer_name = c;}
    public void set_product_id(String id){product_id = id;}   
    public void set_quantity(int q){quantity = q;}
    public void set_total(double tot){total = tot;}     
    public void set_payment_method(String pm){payment_method = pm;}

    public String toCsv(){
        return transaction_id+","+employee_id+","+transaction_date+","+transaction_time+","+customer_name+","+product_id+","+quantity+","+payment_method+","+outlet+","+total;
    }

    public static ArrayList<Transaction> readCsv(){
        try (BufferedReader br = new BufferedReader(new FileReader("Transaction.csv"))) {
            br.readLine();

            String line;
            ArrayList<Transaction> transaction = new ArrayList<>();
            while((line=br.readLine()) != null){
                String[] row = line.split(",");
                Transaction t = new Transaction(
                    Integer.parseInt(row[0]),
                    row[1],
                    row[2],
                    row[3],
                    row[4],
                    row[5],
                    Integer.parseInt(row[6]),
                    row[7],
                    row[8],
                    Double.parseDouble(row[9])
                );
                transaction.add(t);
            }
            return transaction;
        } catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();}
    }

    public static void writeCsv(ArrayList<Transaction> transaction){
        String[] columns = {"transaction_id", "employee_id", "date", "time", "customer_name", "product_id", "quantity", "payment_method", "outlet", "total"};
        try (FileWriter writer = new FileWriter("Transaction.csv")) {
            writer.append(String.join(",", columns)); 
            writer.append("\n");
            for (Transaction t : transaction){
                writer.append(t.toCsv());
                writer.append("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format(
            "| %-4s | %-10s | %-12s | %-8s | %-15s | %-10s | %-5s | %-12s | %-10s | %-10s |\n",
            transaction_id,
            employee_id,
            transaction_date,
            transaction_time,
            customer_name,
            product_id,
            quantity,
            payment_method,
            outlet,
            total
        );
    }
    public static void main(String[] args){
    }
}
