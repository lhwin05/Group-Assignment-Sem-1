import java.util.*;
/*class for login logout
main login screen and options for what employees would like to do after logging in
to run, create a LoginMain object in main and do .run() 
*/

public class LoginMain {
    private EmployeeLogin loginService = new EmployeeLogin();
    private NewEmployeeRegistration registrationService = new NewEmployeeRegistration(); 
    private PerformanceReport performanceReport = new PerformanceReport(); 
    private SalesSystem sale = new SalesSystem();
    private ClockInOut attendance = new ClockInOut();
    private DataAnalytics analytics = new DataAnalytics();
    
    public void run() {
        Scanner input = new Scanner(System.in);
        String loginAnswer;
        boolean logOut = false;
        
        do {
            System.out.println("Logging in? Enter 'Y' if yes.");
            loginAnswer = input.nextLine();
            
            // 4. Program ends 
            if (!(loginAnswer.equalsIgnoreCase("Y"))) {
                System.out.println("Goodbye!");      
                break;
            }
            
            // 1. Log in 
            CurrentEmployee current = loginService.infoLogin(input);
            
            if (current == null) {
                System.out.println();
                continue;
            }
            else {
                // 2. Main menu
                while (!logOut) {
                    if (current.isManager()) {
                        managerOptions(input, current); 
                        System.out.println();
                    }
                    else {
                        otherOptions(input, current);
                        System.out.println();
                    }
                    // 3. Logging out
                    logOut = logOutMenu(input);
                }
            }
        } while (loginAnswer.equalsIgnoreCase("Y"));
    }
    
    private void managerOptions(Scanner input, CurrentEmployee current) {
        System.out.println("Would you like to: ");
        System.out.println("1. Register New Employee");
        System.out.println("2. View Employee Performance Report");
        System.out.println("3. Others");
        System.out.println("Select option: ");
        int choice = input.nextInt();
        System.out.println();
        
        switch (choice) {
            case 1:
                registrationService.registerNewEmployee(input);
                break;
            case 2:
                performanceReport.display();
                break;
            case 3:
                otherOptions(input, current); 
                break; 
        }
    }
    
    private void otherOptions(Scanner input, CurrentEmployee current) {
        System.out.println("Would you like to: ");
        System.out.println("1. Clock In / Clock Out");
        System.out.println("2. Stock Management");
        System.out.println("3. Record Sales");
        System.out.println("4. Search Information");
        System.out.println("5. Edit Information");
        System.out.println("6. View Data Analytics");
        System.out.println("7. Filter and Sort Sales History");

        int choice = input.nextInt();
        input.nextLine();
        switch (choice){
            case 1:
                attendance.clockMenu(input, current);
                break;
            case 2: 
                StockManagement.main_menu(input, current);
                break;
            case 3:
                sale.recordSale(input, current);      
                break;
            case 4:
                SearchInformation.main_menu(input);    
                break;
            case 5:
                EditInformation.main_menu(input);      
                break;
            case 6:
                analytics.run(input);     
                break;
            case 7:
                FilterAndSort.main_menu(input);
                break;
        }

    }

    private boolean logOutMenu(Scanner input){
        System.out.println("Log Out? Enter 'Y' if yes.");
        String logoutAnswer = input.nextLine();
                
        if (logoutAnswer.equalsIgnoreCase("Y")) {
            System.out.println("Log out successful. Goodbye! ");
            System.out.println();
            return true;
        }
        return false;
    }
}
