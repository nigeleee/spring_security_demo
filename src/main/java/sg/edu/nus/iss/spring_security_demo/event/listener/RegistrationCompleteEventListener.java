package sg.edu.nus.iss.spring_security_demo.event.listener;

import java.util.UUID;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.event.RegistrationCompleteEvent;
import sg.edu.nus.iss.spring_security_demo.service.EmailSenderService;
import sg.edu.nus.iss.spring_security_demo.service.UserService;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create verification token for user with link

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user); 

        //send email to user
        String url = event.getApplicationUrl() + "/api/verifyRegistration?token=" + token;
        // String body = "Click on the link to verify your account: " + url;
        String body = "<!DOCTYPE html>" + 
                        "<html>" +
                        "<body>" +
                        "    <div class=\"container\">" +
                        "        <h2>Welcome to our website!</h2>" +
                        "        <p>Thank you for registering. Please click the button below to verify your account:</p>" +
                        "        <a class=\"btn\" href=\"" + url + "\">Verify Account</a>" +
                        "        <p>If the button doesn't work, you can also copy and paste the following link into your browser:</p>" +
                        "        <p><a href=\"" + url + "\">" + url + "</a></p>" +
                        "        <p>If you did not request this registration, you can safely ignore this email.</p>" +
                        "        <p>Best regards,<br>Your Website Team</p>" +
                        "    </div>" +
                        "</body>" +
                        "</html>";
        String subject = "Account Verification";


        try {
            // emailSenderService.sendSimpleEmail(user.getEmail(), body, subject);
            emailSenderService.sendHtmlEmail(user.getEmail(), body, subject);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Error sending verification email to: {}", user.getEmail(), e);
        }
        

        //send verification email method()
        log.info("Click on the link to verify your account {}", url);
    }

}
