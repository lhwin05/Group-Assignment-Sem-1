package assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TransactionWriter {

    private static final String TRANSACTION_FILE = "Transaction.csv";

    public void save(Transaction t) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TRANSACTION_FILE, true))) {
            pw.printf("%d,%s,%s,%s,%s,%s,%d,%s,%.2f\n",
                    t.getTransactionID(),
                    t.getCustomerName(),
                    t.getDate(),
                    t.getTime(),
                    t.getCustomerName(),
                    t.getProductID(),
                    t.getQuantity(),
                    t.getOutlet(),
                    t.getTotal()
            );
        } catch (IOException e) {
            System.out.println("Error writing transaction file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

