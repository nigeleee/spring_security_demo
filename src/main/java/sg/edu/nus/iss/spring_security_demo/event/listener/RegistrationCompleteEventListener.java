package sg.edu.nus.iss.spring_security_demo.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.event.RegistrationCompleteEvent;
import sg.edu.nus.iss.spring_security_demo.service.UserService;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create verification token for user with link

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user); 

        //send email to user
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        //send verification email method()
        log.info("Click on the link to verify your account {}", url);
    }

}
