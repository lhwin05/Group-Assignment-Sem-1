package assignment;

public class Transaction {
    private int transactionID;
    private String employeeID;
    private String date;
    private String time;
    private String customerName;
    private String productID;
    private int quantity;
    private String outlet;
    private double total;

    public Transaction(int transactionID, String employeeID, String date, String time, String customerName, String productID, int quantity,
                       String outlet, double total) {
        this.transactionID = transactionID;
        this.employeeID = employeeID;
        this.date = date;
        this.time = time;
        this.customerName = customerName;
        this.productID = productID;
        this.quantity = quantity;
        this.outlet = outlet;
        this.total = total;
    }

    public int getTransactionID() { return transactionID; }
    public String getEmployeeID() { return employeeID; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getCustomerName() { return customerName; }
    public String getProductID() { return productID; }
    public int getQuantity() { return quantity; }
    public String getOutlet() { return outlet; }
    public double getTotal() { return total; }

    public String toCSV() {
        return transactionID + "," + employeeID + "," + date + "," + time + "," + customerName + "," + productID + "," + quantity + ","
                + outlet + "," + total;
    }
}
