package testFP;

import java.util.*;
/*class for login logout
for managers to register new employees
*/
public class NewEmployeeRegistration {
    private EmployeeReader employeeReader = new EmployeeReader(); 
    
    public void registerNewEmployee(Scanner input) {
        System.out.println("=== New Employee Registration: ===");
        System.out.println("Enter Employee ID: ");
        String newID = input.nextLine();
        
        do {
            if(employeeReader.checkDuplicateID(newID)) {
                System.out.println("ID is already in use. Please enter a new one: ");
                newID = input.nextLine();
            }
        } while(employeeReader.checkDuplicateID(newID)); 
        
        System.out.println("Enter Employee Name: ");
        String newName = input.nextLine();
        System.out.println("Enter role (Part-time / Full-time): ");
        String newRole = input.nextLine(); 
        
        do {
            if (!(newRole.equalsIgnoreCase("Part-time") || newRole.equalsIgnoreCase("Full-time"))) {
                System.out.println("Please enter correct role (Part-time / Full-time): ");
                newRole = input.nextLine();
            }
        } while (!(newRole.equalsIgnoreCase("Part-time") || newRole.equalsIgnoreCase("Full-time"))); 
        
        System.out.println("Set Password: ");
        String newPass = input.nextLine();
        
        Employee newEmployee = new Employee(newID, newName, newRole, newPass); 
        employeeReader.saveNewEmployee(newEmployee);
        System.out.println("Employee successfully registered!");   
    }
}
