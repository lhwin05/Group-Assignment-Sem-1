package assignment;

public class Attendance {
    private String employeeID;
    private String outlet;
    private String date;
    private String clockIn;
    private String clockOut;

    public Attendance(String employeeID, String outlet, String date, String clockIn, String clockOut) {
        this.employeeID = employeeID;
        this.outlet = outlet;
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
    }

    public String getEmployeeID() { return employeeID; }
    public String getOutlet() { return outlet; }
    public String getDate() { return date; }
    public String getClockIn() { return clockIn; }
    public String getClockOut() { return clockOut; }

    public String toCSV() {
        return employeeID + "," + outlet + "," + date + "," + clockIn + "," + clockOut;
    }
}

