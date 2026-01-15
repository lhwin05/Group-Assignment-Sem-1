import java.util.*;

public class EmployeeLogin {
    private EmployeeReader employeeReader = new EmployeeReader();
    private CurrentEmployee current = null; //null because no user right now
    
    public CurrentEmployee infoLogin(Scanner input) {
        System.out.println("Enter your outlet (KLCC / Lot_10):");
        String outlet = input.nextLine();
        
        do {
            if (!(outlet.equals("KLCC") || outlet.equals("Lot_10"))) {
                System.out.println("Please enter correct outlet. ");
                outlet = input.nextLine();
            }
        } while (!(outlet.equals("KLCC") || outlet.equals("Lot_10")));
        
        System.out.println("Enter Employee ID: ");
        String employeeID = input.nextLine(); 
        System.out.println("Enter Password: ");
        String password = input.nextLine();
        
        Employee employee = employeeReader.authenticate(employeeID, password);
        if (employee != null) {
            current = new CurrentEmployee(employee.getEmployeeID(), employee.getPassword(), employee.getRole()); //now there's a user so create object to save the current user
            current.setOutlet(outlet); //for other features to get outlet do current.getOutlet();
            
            System.out.println();
            System.out.println("Successful login!");
            System.out.println("Welcome, " + employee.getEmployeeName() + " (" + employee.getEmployeeID() + ")");
            return current; //returns who the current user currently logged in is
        }
        else {
            System.out.println("Login Failed: Invalid ID or Password.");
            return null; 
        }
    }
    
    public CurrentEmployee getCurrent() {
        return current; 
    }
}
