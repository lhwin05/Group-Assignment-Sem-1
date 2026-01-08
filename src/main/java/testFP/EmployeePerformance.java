package testFP;

/*class for employee performance metrics
creates objects to store employee performances
*/
public class EmployeePerformance {
    private String employeeID;
    private String employeeName;
    private double totalSales;
    private int transactionCount; 
    private String outlet;
    
    public EmployeePerformance(String employeeID, String employeeName, double totalSales, int transactionCount, String outlet) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.totalSales = totalSales;
        this.transactionCount = transactionCount; 
        this.outlet = outlet; 
    }
    
    //getters
    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public double getTotalSales() { return totalSales; }
    public int getTransactionCount() { return transactionCount; }
    public String getOutlet() { return outlet; }
    
}
