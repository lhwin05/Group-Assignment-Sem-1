package assignment;

import java.util.Scanner;

public class DataAnalyticsTest {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        DataAnalyticsMain analytics = new DataAnalyticsMain();

        boolean running = true;

        while (running) {
            analytics.run(input);

            System.out.print("\nDo you want to perform another analysis? (Y/N): ");
            String c = input.nextLine();
            if (c.equalsIgnoreCase("N")) {
                running = false;
            }
            System.out.println();
        }

        System.out.println("Exiting Data Analytics.");
        input.close();
    }
}
