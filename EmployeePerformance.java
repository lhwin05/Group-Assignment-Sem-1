public class EmployeePerformance {
    private String employeeID;
    private String employeeName;
    private double totalSales;
    private int transactionCount; 
    
    public EmployeePerformance(String employeeID, String employeeName, double totalSales, int transactionCount) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.totalSales = totalSales;
        this.transactionCount = transactionCount; 
    }
    
    //getters
    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public double getTotalSales() { return totalSales; }
    public int getTransactionCount() { return transactionCount; }
}
