import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.concurrent.*;


public class EmailSender {
    private static String getTodayDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = today.format(formatter);
        return date;
    }

    private static Session initialiseSession(){
        final String username = "honwinlai@gmail.com"; 
        final String password = "bnue ewpx kzvr hfbh";   

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); 
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); 

        // Get session
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    private static void sendEmail(Session session){
        try {
            // Sender, recipient
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("honwinlai@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("alvinlai7789@gmail.com"));
            message.setSubject("Daily Transaction File " + getTodayDate());

            // Compose body
            BodyPart body = new MimeBodyPart(); 
            body.setText("Attached below is the transaction file for today's sales. Kindly read through and validate. ");

            // Create today transaction file
            String today = getTodayDate();
            String dailyReport = Receipt.daily_sales_report(today);

            // Create attachment
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(dailyReport);

            // Send message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);
            multipart.addBodyPart(attachment);
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendDailyTransaction(){
        Session session = initialiseSession();
        sendEmail(session);
    }

    // Calculate delay in seconds
    private static long calculateInitialDelay(LocalTime sendTime){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.with(sendTime);

        if (now.compareTo(nextRun) > 0){
            nextRun = nextRun.plusDays(1);
        }

        // return in seconds
        return Duration.between(now, nextRun).toSeconds();    
    }

    // Email logic tested locally and run successfully 
    private static void run(){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        LocalTime sendTime = LocalTime.of(22,15);
        Runnable emailTask = () -> {
            try {sendDailyTransaction();}
            catch (Exception e) {e.printStackTrace();}
        };

        long initialDelay = calculateInitialDelay(sendTime);
        scheduler.scheduleAtFixedRate(
            emailTask, 
            initialDelay, 
            TimeUnit.DAYS.toSeconds(1), 
            TimeUnit.SECONDS);
    }
    public static void main(String[] args) {
        run();
    }
}