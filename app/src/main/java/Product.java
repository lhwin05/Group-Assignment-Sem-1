import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Product {
    private String product_id;
    private double selling_price;
    private double profit;

    public Product(String id, double price, double pft){
        product_id = id;
        selling_price = price;
        profit = pft;
    }

    public String get_product_id(){return product_id;}
    public double get_selling_price(){return selling_price;}
    public double get_profit(){return profit;}

    public static ArrayList<Product> readCsv(){
        try (BufferedReader br = new BufferedReader(new FileReader("Products.csv"))) {
            br.readLine();

            String line;
            ArrayList<Product> product = new ArrayList<>();
            while((line=br.readLine()) != null){
                String[] row = line.split(",");
                Product p = new Product(
                    row[0], 
                    Double.parseDouble(row[1]), 
                    Double.parseDouble(row[2])
                );
                product.add(p);
            }
            return product;
        } catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();}
    }

}
