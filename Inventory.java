import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory {
    private String product_id;
    private int stock_lot10;
    private int stock_klcc;

    public Inventory(String id, int stk1, int stk2){
        product_id = id;
        stock_lot10 = stk1;
        stock_klcc = stk2;
    }

    public String get_product_id(){return product_id;}
    public int get_stock_lot10(){return stock_lot10;} 
    public int get_stock_klcc(){return stock_klcc;}
    public void set_stock_lot10(int stk1){stock_lot10 = stk1;}
    public void set_stock_klcc(int stk2){stock_klcc = stk2;}

    @Override
    public String toString(){
        return product_id+","+stock_lot10+","+stock_klcc;
    }

    public static ArrayList<Inventory> readCsv(){
        try (BufferedReader br = new BufferedReader(new FileReader("Inventory.csv"))) {
            br.readLine();

            String line;
            ArrayList<Inventory> inventory = new ArrayList<>();
            while((line=br.readLine()) != null){
                String[] row = line.split(",");
                Inventory t = new Inventory(
                    row[0], 
                    Integer.parseInt(row[1]), 
                    Integer.parseInt(row[2])
                );
                inventory.add(t);
            }
            return inventory;
        } catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();}
    }

    public static void writeCsv(ArrayList<Inventory> inventory){
        String[] columns = {"product_id", "Lot_10", "KLCC"};
        try (FileWriter writer = new FileWriter("Inventory.csv")) {
            writer.append(String.join(",", columns)); 
            writer.append("\n");
            for (Inventory i : inventory){
                writer.append(i.toString());
                writer.append("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Inventory i = new Inventory("DW-5600",20,20);
        System.out.println(i.toString()); 
    }
}
