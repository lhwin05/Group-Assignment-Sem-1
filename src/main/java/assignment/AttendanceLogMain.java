package assignment;
import java.util.Scanner;

public class AttendanceLogMain {
    private ClockInOut clockInOut = new ClockInOut();

    public void run() {
        Scanner input = new Scanner(System.in);
        clockInOut.clockMenu(input);
    }

    public static void main(String[] args) {
        AttendanceLogMain app = new AttendanceLogMain();
        app.run();
    }
}
