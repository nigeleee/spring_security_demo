package sg.edu.nus.iss.spring_security_demo.service;

import org.springframework.stereotype.Service;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.model.UserModel;

@Service
public interface UserService {

    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificaionToken(String token);

    User getCurrentUser();

    User getUserByEmail(String email);

    boolean validateUserPassword(User user, String password);

    
}
