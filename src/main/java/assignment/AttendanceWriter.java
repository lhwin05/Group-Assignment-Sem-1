package assignment;
import java.io.*;

public class AttendanceWriter {
    private static final String FILE = "Attendance.csv";

    public void saveAttendance(Attendance attendance) {
        try (PrintWriter write = new PrintWriter(new FileOutputStream(FILE, true))) {

            write.println(attendance.toCSV());

        }catch (IOException e) {
            System.out.println("Problem writing attendance file.");
        }
    }
}
