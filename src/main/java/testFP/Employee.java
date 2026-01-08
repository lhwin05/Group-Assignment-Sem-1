package testFP;

/*class for login logout
creates objects to stores employees
toCSV is to write all the parameters an employee has into CSV
*/
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
