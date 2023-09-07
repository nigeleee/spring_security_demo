package sg.edu.nus.iss.spring_security_demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    
    @Autowired
    private JavaMailSender mailSender;

    // @Value("${email}")
    // private String email;

    public void sendSimpleEmail(String toEmail, String body, String subject) throws MessagingException {
        
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("nigeleee@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message); 
        
        System.out.println("Mail sent");
    }

    public void sendHtmlEmail(String toEmail, String body, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("nigeleee@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true); // Set HTML content

        mailSender.send(message);

        System.out.println("HTML email sent");
    }

}
