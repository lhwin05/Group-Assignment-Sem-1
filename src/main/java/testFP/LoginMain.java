package testFP;

import java.util.*;
/*class for login logout
main login screen and options for what employees would like to do after logging in
to run, create a LoginMain object in main and do .run() 
*/
public class LoginMain {
    private EmployeeLogin loginService = new EmployeeLogin();
    private NewEmployeeRegistration registrationService = new NewEmployeeRegistration(); 
    private PerformanceReport performanceReport = new PerformanceReport(); 
    
    public void run() {
        Scanner input = new Scanner(System.in);
        String loginAnswer;
        String logoutAnswer; 
        
        do {
            System.out.println("Logging in? Enter 'Y' if yes.");
            loginAnswer = input.nextLine();
            
            if (!(loginAnswer.equalsIgnoreCase("Y"))) {
                System.out.println("Goodbye!");
                break;
            }
            
            CurrentEmployee current = loginService.infoLogin(input);
            
            if (current == null) {
                System.out.println();
                continue;
            }
            else {
                while (true) {
                    if (current.isManager()) {
                        managerOptions(input); 
                        System.out.println();
                    }
                    else {
                        otherOptions();
                        System.out.println();
                    }
                
                    System.out.println("Log Out? Enter 'Y' if yes.");
                    logoutAnswer = input.nextLine();
                
                    if (logoutAnswer.equalsIgnoreCase("Y")) {
                        System.out.println("Log out successful.");
                        System.out.println();
                        break; 
                    }
                }
            }
            
        } while (loginAnswer.equalsIgnoreCase("Y"));
    }
    
    private void managerOptions(Scanner input) {
        System.out.println("Would you like to: ");
        System.out.println("1. Register New Employee");
        System.out.println("2. View Employee Performance Report");
        System.out.println("3. Others");
        System.out.println("Select option: ");
        int choice = input.nextInt();
        input.nextLine(); 
        System.out.println();
        
        switch (choice) {
            case 1:
                registrationService.registerNewEmployee(input);
                break;
            case 2:
                performanceReport.display();
                break;
            case 3:
                otherOptions(); 
                break; 
        }
    }
    
    private void otherOptions() {
        System.out.println("Would you like to: ");
        System.out.println("1. Clock In / Clock Out");
        System.out.println("2. Manage Stock");
        System.out.println("3. Record Sales");
        System.out.println("4. Search Information");
        System.out.println("5. Edit Information");
        System.out.println("6. View Data Analytics");
        System.out.println("7. Filter and Sort Sales History");
        System.out.println("here can add objects of the other features cam stock manage etc semua tu macam dalam manager options");
    }
}
