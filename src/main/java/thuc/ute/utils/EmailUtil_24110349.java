package thuc.ute.utils;

import java.util.Properties;
import java.util.Random;
import java.security.SecureRandom;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil_24110349 {
    // Cấu hình tài khoản gửi
    private static final String SENDER_EMAIL = "trt571983@gmail.com"; 
    private static final String SENDER_PASSWORD = "oubq irzo pgde mest"; // 16 ký tự App Password

    // 1. Hàm sinh ngẫu nhiên mã OTP gồm 6 chữ số
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // 2. Hàm gửi email chứa mã OTP
    public static boolean sendOtpEmail(String recipientEmail, String otpCode) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Xác thực tài khoản gửi
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mã xác thực đăng ký tài khoản (OTP)");
            message.setContent("<h3>Mã xác thực OTP của bạn là: <b style='color:red;'>" + otpCode + "</b></h3>"
                    + "<p>Mã này có hiệu lực trong vòng 5 phút. Vui lòng không chia sẻ cho bất kỳ ai.</p>", 
                    "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
