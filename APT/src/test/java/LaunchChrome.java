import com.sun.mail.smtp.SMTPTransport;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.Test;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class LaunchChrome extends BaseClass {

    WebDriver driver;
    String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.50 Safari/537.36";

    List<File> generatedFiles = new ArrayList<>();

    @Test(priority = -1)
    public void browserLaunch() {


        System.out.println("Before initializing the driver.");
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("download.default_directory", System.getProperty("user.dir") + System.getProperty("file.separator") + "report");
        System.out.println(System.getProperty("user.dir"));

        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        options.addArguments(String.valueOf(PageLoadStrategy.NORMAL));
//                options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--proxy-server='direct://'");
        options.addArguments("--proxy-bypass-list=*");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("user-agent=" + userAgent);
//            options.addArguments("--user-data-dir="+System.getProperty("user.dir") +System.getProperty("file.separator") + "GoogleProfile");
        options.setExperimentalOption("prefs", p);
        options.setCapability("goog:loggingPrefs", logs);
        WebDriverManager.chromedriver().setup();
        System.out.println("After initializing the driver.");
        driver = new ChromeDriver(options);
        driver.get("https://pentest-tools.com/pricing?utm_campaign=ab-home-tools-cta&utm_source=homepage-v1&utm_medium=website&utm_content=main-button&utm_term=start-pentesting-now");
        driver.manage().window().maximize();
        driver.manage().window().maximize();
    }

    @Test(priority = 0)
    public void loginPage() throws InterruptedException {

        driver.findElement(By.xpath("//a[text()=' Log in ']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("invictidrudge@gmail.com");
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("Adhianna@7358");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Thread.sleep(10000);
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@data-test-id='interactive-frame']")));
        driver.findElement(By.xpath("//div[@id='interactive-close-button']")).click();
        driver.switchTo().defaultContent();
    }

    @Test(priority = 1)
    public void scans() throws InterruptedException {
        driver.findElement(By.xpath("//span[text()='Scans']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//a[@class='!ptt-btn-primary !ptt-mt-5 !ptt-w-fit']")).click();
        driver.findElement(By.xpath("//h4[text()='Website Vulnerability Scanner']")).click();
        driver.findElement(By.xpath("//input[@name='target']")).sendKeys("https://console.cloud.google.com/projectselector2/net-security/securitypolicies/list?_ga=2.10069245.936492860.1607995107-366914458.1596215128&pli=1&supportedpurview=project");
        driver.findElement(By.xpath("//label[@class='col-xs-12 no-padding']//span[@class='lbl small']")).click();
        driver.findElement(By.xpath("//span[contains(text(),'I am authorized to scan this target and I agree to')]")).click();
        driver.findElement(By.xpath("//span[@id='start_button_text']")).click();
    }

    @Test(priority = 2)
    public void downLoadReport() throws InterruptedException {
        Thread.sleep(50000);
        driver.findElement(By.xpath("//a[@id='download_result_link']")).click();

        driver.findElement(By.xpath("//button[text()='Export']")).click();
        Thread.sleep(5000);
        File latestFile = getLatestFile(System.getProperty("user.dir") + System.getProperty("file.separator") + "report", ".pdf");


// Format the current date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String formattedDate = dateFormat.format(currentDate);

// Rename the file with a unique name
        if (latestFile != null && latestFile.getName().toLowerCase().endsWith(".pdf")) {
            try {
                // Close any open resources associated with the latestFile
                // For example, close any FileInputStream or other streams

                // Delete the old report file if it exists
                File oldReportFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "report",
                        "Nurture_RestoreMe_" + formattedDate + "(Page Sessions).pdf");
                if (oldReportFile.exists()) {
                    boolean deleteSuccess = oldReportFile.delete();
                    if (deleteSuccess) {
                        System.out.println("Deleted old Nurture_RestoreMe_" + formattedDate + "(Page Sessions).pdf");
                    } else {
                        System.out.println("Failed to delete old Nurture_RestoreMe_" + formattedDate + "(Page Sessions).pdf");
                    }
                }

                // Create the new report file
                File newReportFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "report",
                        "Nurture_RestoreMe_" + formattedDate + "(Page Sessions).pdf");

                try {
                    FileUtils.moveFile(latestFile, newReportFile);

                    System.out.println("Renamed the new report to: " + newReportFile.getName());
                    generatedFiles.add(newReportFile);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (latestFile == null) {
            System.out.println("No PDF file found in the download directory.");
        } else {
            System.out.println("The latest file is not a PDF file: " + latestFile.getName());
        }
        System.out.println("Reached the try block successfully.");
        Thread.sleep(20000);
    }






        @Test(priority = 3)
        public static void sendEmailWithAttachment(List<File> attachmentFiles, String dateRange) {
            try {


            String gmailUsername = "dineshkumarsuriya56@gmail.com";
            String appPassword = "Dinnar@2356";

            // Set the email properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Create a session with App Password authentication
            Session session = Session.getInstance(props);
            MimeMessage message = new MimeMessage(session);

            // Set the message details
            message.setFrom(new InternetAddress(gmailUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("invictidrudge@gmail.com"));

            message.setSubject("Nurture Restore Me App Analytics Report - " + dateRange);

            // Create the message body part for the text content
            Multipart multipart = new MimeMultipart();
            BodyPart textPart = new MimeBodyPart();
            textPart.setText("Hello Dr.Garg and Adriana,\n" +
                    "\n" +
                    "Attached is the analytics for the last 7 days " + (dateRange)+" Also, this one has the page view analytics & User Acquisition reports.\n" +
                    "\n" +
                    "Let us know if you have any queries.\n" +
                    "\n" +
                    "Thank You");
            multipart.addBodyPart(textPart);


            // Create the message body part for the attachment
            for (File attachmentFile : attachmentFiles) {
                if (attachmentFile.getName().toLowerCase().endsWith(".pdf")) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    FileDataSource fileDataSource = new FileDataSource(attachmentFile.getAbsolutePath());
                    attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                    attachmentPart.setFileName(attachmentFile.getName());
                    multipart.addBodyPart(attachmentPart);
                }
            }

            // Set the content of the message to the Multipart object
            message.setContent(multipart);

            // Get the SMTP transport
            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");

            // Connect using the App Password
            transport.connect("smtp.gmail.com", gmailUsername, appPassword);

            // Send the email
            transport.sendMessage(message, message.getAllRecipients());

            // Close the transport
            transport.close();
            System.out.println("Email sent successfully with attachment.");

            // Delete the PDF file after sending the email
            for (File attachmentFile : attachmentFiles) {
                if (attachmentFile.exists() && attachmentFile.delete()) {
                    System.out.println("Deleted attached PDF file: " + attachmentFile.getName());
                } else {
                    System.out.println("Failed to delete attached PDF file: " + attachmentFile.getName());
                }
            }

        } catch (Exception e) {
            System.out.println("Error sending email:" + e.getMessage());
        }
    }


}






