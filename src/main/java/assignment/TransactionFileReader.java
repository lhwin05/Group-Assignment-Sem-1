package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TransactionFileReader {

    private static final String TRANSACTION_FILE = "Transaction.csv";

    // get next available transaction ID
    public int getNextTransactionID() {
        int maxID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                try {
                int id = Integer.parseInt(parts[0]);
                if (id > maxID) maxID = id;
            } catch (NumberFormatException e) {
                }
            }
        } catch (IOException e) {

        } catch (NumberFormatException e) {
            System.out.println("Error parsing transaction ID: " + e.getMessage());
        }

        return maxID + 1;
    }
}


