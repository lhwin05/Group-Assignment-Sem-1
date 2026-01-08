package testFP;

import java.io.*;
import java.util.*;
/*class for employee performance metrics
reads from Employee.csv and Transaction.csv to find needed info the create EmployeePerformance object 
*/
public class PerformanceReader {
    private int loadEmployees(String [] employeeIDs, String [] employeeNames) {
        int count = 0;
        
        try {
            Scanner read = new Scanner(new FileInputStream("Employee.csv"));
            boolean headerLine = true;
            
            while (read.hasNextLine()) {
                String line = read.nextLine(); 
                
                if (headerLine) {
                    headerLine = false;
                    continue; 
                }
                
                String [] employeeInfo = line.split(",");
                employeeIDs[count] = employeeInfo[0];
                employeeNames[count] = employeeInfo[1];
                count++;
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
        }
        return count; 
    }
    
    private double calculateTotalSales(String employeeID) {
        double total = 0.0;
        
        try {
            Scanner read = new Scanner(new FileInputStream("Transaction.csv"));
            boolean headerLine = true;
            
            while (read.hasNextLine()) {
                String line = read.nextLine();
                
                if (headerLine) {
                    headerLine = false;
                    continue;
                }
                
                String [] salesInfo = line.split(",");
                if (employeeID.equals(salesInfo[1])) {
                    total += Double.parseDouble(salesInfo[8]);
                }
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return total; 
    }
    
    private int countTransactions(String employeeID) {
        int count = 0; 
        
        try {
            Scanner read = new Scanner(new FileInputStream("Transaction.csv"));
            boolean headerLine = true;
            
            while (read.hasNextLine()) {
                String line = read.nextLine();
                
                if (headerLine) {
                    headerLine = false;
                    continue;
                }
                
                String [] transactionInfo = line.split(",");
                if (employeeID.equals(transactionInfo[1])) {
                    count++;
                }
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return count; 
    }
    
    private String employeeOutlet(String employeeID) {
        try {
            Scanner read = new Scanner(new FileInputStream("Transaction.csv"));
            boolean headerLine = true;
            
            while (read.hasNextLine()) {
                String line = read.nextLine();
                
                if (headerLine) {
                    headerLine = false;
                    continue;
                }
                
                String [] outletInfo = line.split(",");
                if (employeeID.equals(outletInfo[1])) {
                    return outletInfo[7];
                }
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return null;
    }
    
    public EmployeePerformance [] getAllEmployeePerformance() {
        String [] employeeIDs = new String [100];
        String [] employeeNames = new String [100];
        int employeeCount = loadEmployees(employeeIDs, employeeNames);
        
        EmployeePerformance [] performance = new EmployeePerformance[employeeCount]; 
        int performanceCount = 0; 
        
        for (int i = 0; i < employeeCount; i++) {
            double totalSales = calculateTotalSales(employeeIDs[i]);
            int transactionCount = countTransactions(employeeIDs[i]); 
            String outlet = employeeOutlet(employeeIDs[i]);
            
            if (transactionCount > 0) {
                performance[performanceCount] = new EmployeePerformance(employeeIDs[i], employeeNames[i], totalSales, transactionCount, outlet); 
                performanceCount++; 
            }
        }
        performance = sortPerformances(performance, performanceCount); 
        return performance; 
    }
    
    private EmployeePerformance [] sortPerformances(EmployeePerformance [] performances, int performanceCount) {
        EmployeePerformance [] result = new EmployeePerformance[performanceCount];
        for (int i = 0; i < performanceCount; i++) {
            result[i] = performances[i];
        }
        
        for (int pass = 1; pass < performanceCount; pass++) {
            for (int i = 0; i < (performanceCount - 1); i++) {
                if (result[i].getTotalSales() < result[i + 1].getTotalSales()) {
                    EmployeePerformance temp = result[i];
                    result[i] = result[i + 1];
                    result[i + 1] = temp; 
                }
            }
        }
        return result; 
    }
}
