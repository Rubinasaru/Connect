package com.example.demo.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



@Service
public class otpService {
		private JavaMailSender mailSender;
		
		public otpService(JavaMailSender mailSender) {
			this.mailSender=mailSender;
		}

	    @Value("${spring.mail.username}")
	    private String senderEmail;

	    @Value("${otp.expiry.minutes}")
	    private int otpExpiryMinutes;

	    @Value("${frontend.url}")
	    private String frontendUrl;

	    private final Map<String, OtpData> otpStorage = new HashMap<>(); // Temporary OTP storage

	    // Class to hold OTP and timestamp
	    private static class OtpData {
	        String otp;
	        LocalDateTime createdAt;

	        OtpData(String otp, LocalDateTime createdAt) {
	            this.otp = otp;
	            this.createdAt = createdAt;
	        }
	    }

	    // Generate an 8-digit OTP
	    public String generateOTP(){
	        Random random = new Random();
	        int otp = 100000 + random.nextInt(900000); // Generates 8-digit OTP
	        return String.valueOf(otp);
	    }

	    // Send OTP to the provided email
	    public void sendOtpEmail(String email) throws MessagingException, UnsupportedEncodingException {
	        String otp = generateOTP();
	        otpStorage.put(email, new OtpData(otp, LocalDateTime.now()));

	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(senderEmail, "Connect"); // Friendly sender name
	        helper.setTo(email);
	        helper.setReplyTo(senderEmail);
	        helper.setSubject("Your OTP for Connect Registration");
	        helper.setText(
	                "<h2>Welcome to Connect!</h2>"+
	                        "<p>Thank you for registering. Your One-Time Password (OTP) is:</p>" +
	                        "<p style='font-size: 24px; color: #007bff;'><b>" + otp + "</b></p>" +
	                        "<p>This OTP is valid for " + otpExpiryMinutes + " minutes. Please do not share it with anyone.</p>" +
	                        "<p>If you did not request this OTP, please ignore this email or contact support.</p>" +
	                        "<p>Best regards,<br>Connect Team</p>",
	                true
	        );
	        try {
	            mailSender.send(message);
	            System.out.println("OTP email sent to " + email + " from " + senderEmail);
	        } catch (Exception e) {
	            System.err.println("Failed to send OTP email to " + email + ": " + e.getMessage());
	            throw new MessagingException("Failed to send OTP email: " + e.getMessage() + "!", e);
	        }
	    }

	    // Verify OTP for the given email
	    public boolean verifyOtp(String email, String otp) {
	        OtpData otpData = otpStorage.get(email);
	        if (otpData == null) {
	            System.out.println("No OTP found for " + email);
	            return false;
	        }

	        LocalDateTime now = LocalDateTime.now();
	        long minutesElapsed = ChronoUnit.MINUTES.between(otpData.createdAt, now);
	        if (minutesElapsed > otpExpiryMinutes) {
	            System.out.println("OTP for " + email + " expired. Created at: " + otpData.createdAt + ", Now: " + now);
	            otpStorage.remove(email);
	            return false;
	        }

	        if (otpData.otp.equals(otp)) {
	            System.out.println("OTP verified for " + email + "!");
	            otpStorage.remove(email);
	            return true;
	        }

	        System.out.println("Invalid OTP for " + email + "!");
	        return false;
	    }
	    
	 
}
