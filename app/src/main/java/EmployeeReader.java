import java.util.*;
import java.io.*;


public class EmployeeReader {
    private int loadEmployees(String [] employeeID, String [] employeeName, String [] role, String [] password) {
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
                employeeID[count] = employeeInfo[0];
                employeeName[count] = employeeInfo[1];
                role[count] = employeeInfo[2];
                password[count] = employeeInfo[3]; 
                
                count++; 
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        return count; 
    }
    
    public Employee [] getAllEmployeeInfo() {
        String [] employeeID = new String[100];
        String [] employeeName = new  String[100];
        String [] role = new String[100];
        String [] password = new String[100]; 
        
        int count = loadEmployees(employeeID, employeeName, role, password); 
        
        Employee [] allEmployeeInfo = new Employee[count];
        int infoCount = 0;
        
        for (int i = 0; i < count; i++) {
            allEmployeeInfo[infoCount] = new Employee(employeeID[i], employeeName[i], role[i], password[i]); 
            infoCount++; 
        }
        return allEmployeeInfo; 
    }
    
    public void saveNewEmployee(Employee employee) {
        try {
            PrintWriter write = new PrintWriter(new FileOutputStream("Employee.csv", true));
            write.println(employee.toCSV());
            write.close();
        }
        catch (IOException e) {
            System.out.println("Problem with file output.");
        }
    }
    
    public boolean checkDuplicateID(String employeeID) {
        Employee [] employees = getAllEmployeeInfo();
        for (Employee emp : employees) {
            if (emp.getEmployeeID().equals(employeeID)) {
                return true;
            }
        }
        return false; 
    }
    
    public Employee authenticate(String employeeID, String password) {
        Employee [] employees = getAllEmployeeInfo();
        for (Employee emp : employees) {
            if (emp.getEmployeeID().equals(employeeID)) {
                if (emp.getPassword().equals(password)) {
                    return emp;
                }
            }
        }
        return null; 
    }
}
