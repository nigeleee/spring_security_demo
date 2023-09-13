package sg.edu.nus.iss.spring_security_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.repository.UserRepo;

@Service
public class Oauth2Service {
    @Autowired
    private UserRepo userRepo;

    public User findOrCreateUser(String email, String firstName, String lastName) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            // Create the user if they do not exist
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            userRepo.save(user);
        }
        return user;
    }
}    
