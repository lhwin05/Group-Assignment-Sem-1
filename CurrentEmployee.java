public class CurrentEmployee {
    private String employeeID;
    private String employeeName;
    private String role;
    private String outlet;
    
    public CurrentEmployee(String employeeID, String employeeName, String role) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.role = role;
        this.outlet = "";
    }
    
    public String getEmployeeID() { return employeeID; }
    public String getEmployeeName() { return employeeName; }
    public String getRole() { return role; }
    public String getOutlet() { return outlet; }
    
    public boolean isManager() { return role.equalsIgnoreCase("Manager"); }
    
    public void setOutlet(String outlet) { this.outlet = outlet; }
}
