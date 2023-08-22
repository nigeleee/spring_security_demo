package sg.edu.nus.iss.spring_security_demo.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import sg.edu.nus.iss.spring_security_demo.entity.User;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    
    private User user;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) { 
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;  
    }
}
