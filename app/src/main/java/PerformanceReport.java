/*class for employee performance metrics
displays performance report
create PerformanceReport object and do .display to show performance report
*/
public class PerformanceReport {
    private PerformanceReader performanceReader = new PerformanceReader(); 
    
    public void display() {
        System.out.println("Employee Performance Metrics Report");
        System.out.println("============================================================================================");
        
        EmployeePerformance [] performances = performanceReader.getAllEmployeePerformance();
        System.out.printf("%10s %17s %19s %22s %18s\n", "Rank", "Employee ID", "Employee Name", "Total Sales (RM)", "Transactions");
        
        for (int i = 0; i < performances.length; i++) {
            EmployeePerformance employee = performances[i];
            System.out.printf("%10s %17s %19s %22.2f %18d\n", (i + 1), employee.getEmployeeID(), employee.getEmployeeName(), employee.getTotalSales(), employee.getTransactionCount());
        }
    }
}
