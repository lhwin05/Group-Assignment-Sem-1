package assignment;
import java.util.*;

public class DataAnalyticsMain {

    private DataAnalytics service = new DataAnalytics();

    public void run(Scanner input) {
        System.out.println("=== Data Analytics ===");
        System.out.println("1. Total Sales Per Day");
        System.out.println("2. Most Sold Product");
        System.out.println("3. Average Daily Revenue");
        System.out.print("Choose option: ");

        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                service.totalSalesPerDay();
                break;
            case 2:
                service.mostSoldProduct();
                break;
            case 3:
                service.averageDailyRevenue();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
