package assignment;
import java.io.*;
import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class ClockInOut {

    private static final String FILE = "Attendance.csv";
    private AttendanceWriter writer = new AttendanceWriter();

    private static final DateTimeFormatter date_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter time_format = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void clockMenu(Scanner input) {

        System.out.println("1. Clock In");
        System.out.println("2. Clock Out");
        System.out.print("Select option: ");
        int choice = input.nextInt();
        input.nextLine();

        System.out.println("Employee ID: ");
        String employeeID = input.nextLine();

        System.out.println("Outlet: ");
        String outlet = input.nextLine();

        if (choice == 1) {
            clockIn(employeeID, outlet);
        } else if (choice == 2) {
            clockOut(employeeID);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    //CLOCK IN
    public void clockIn(String employeeID, String outlet) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        Attendance attendance = new Attendance(employeeID, outlet, date.format(date_format), time.format(time_format), "-");

        writer.saveAttendance(attendance);

        System.out.println("\nAttendance Log - Clock In");
        System.out.println("Employee ID : " + employeeID);
        System.out.println("Outlet      : " + outlet);
        System.out.println("Date        : " + date.format(date_format));
        System.out.println("Time        : " + time.format(time_format));
    }

    // CLOCK OUT
    public void clockOut(String employeeID) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        String today = LocalDate.now().format(date_format);
        LocalTime outTime = LocalTime.now();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");
                if (data[0].equals(employeeID) && data[2].equals(today.formatted(date_format)) && data[4].equals("-")) {

                    LocalTime inTime = LocalTime.parse(data[3], time_format);
                    double hours = Duration.between(inTime, outTime).toMinutes() / 60.0;
                    hours = Math.round(hours * 10.0) / 10.0;

                    data[4] = outTime.format(time_format);
                    lines.add(String.join(",", data));

                    System.out.println("\nAttendance Log - Clock Out");
                    System.out.println("Employee ID : " + employeeID);
                    System.out.println("Date        : " + today.formatted(date_format));
                    System.out.println("Time        : " + outTime.format(time_format));
                    System.out.println("Total Hours : " + hours + " hours");

                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        if (!updated) {
            System.out.println("No clock in yet.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error updating file.");
        }
    }
}