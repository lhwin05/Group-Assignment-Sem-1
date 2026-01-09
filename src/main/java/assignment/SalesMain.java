package assignment;
import java.util.Scanner;

public class SalesMain {

    private SalesSystem sales = new SalesSystem();

    public void run() {
        Scanner input = new Scanner(System.in);
        sales.recordSale(input);
    }

    public static void main(String[] args) {
        new SalesMain().run();
    }
}

