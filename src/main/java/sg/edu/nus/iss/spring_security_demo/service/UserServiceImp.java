package sg.edu.nus.iss.spring_security_demo.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.entity.VerificationToken;
import sg.edu.nus.iss.spring_security_demo.model.UserModel;
import sg.edu.nus.iss.spring_security_demo.repository.UserRepo;
import sg.edu.nus.iss.spring_security_demo.repository.VerificationTokenRepo;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepo.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);

        verificationTokenRepo.save(verificationToken);
    }

    @Override
    public String validateVerificaionToken(String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(token);

        if(verificationToken == null) {
            return "invalid token";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0 ) {
            verificationTokenRepo.delete(verificationToken);
            return "expired";
        } 
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }
    
}
