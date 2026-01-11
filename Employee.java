public class Employee {
    private String employeeID;
    private String employeeName;
    private String role;
    private String password;
    
    public Employee(String employeeID, String employeeName, String role, String password) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.role = role;
        this.password = password;
    }
    
    //getters
    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    
    public String toCSV() {
        String line = employeeID + "," + employeeName + "," + role + "," + password;
        return line; 
    }
}


// 1. Refactor toCsv() so that can write modified array into csv 