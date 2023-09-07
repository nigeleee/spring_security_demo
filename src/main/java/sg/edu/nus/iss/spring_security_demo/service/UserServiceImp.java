package sg.edu.nus.iss.spring_security_demo.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        // Check if the email already exists
        if (userRepo.findByEmail(userModel.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Create and save the new user
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        // user.setPassword(userModel.getPassword());

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

        if (verificationToken == null) {
            return "invalid token";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0) {
            verificationTokenRepo.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }

    @Override
    public User getCurrentUser() {
        // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> in getCurrentUser()");
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
        //     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Authentication: " + authentication.isAuthenticated());

        //     String username = authentication.getName();
        //     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Authenticated user: " + username);

        //     return userRepo.findByEmail(username);
        // }
        // return null; // No authenticated user
        
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> in getCurrentUser()");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>> Authentication is successful.");
            System.out.println(">>>>>>>>>>>>>>>>>> Authentication object: " + authentication);
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> Principal is an instance of UserDetails.");

                UserDetails userDetails = (UserDetails) principal;
                String usernameFromPrincipal = userDetails.getUsername();
                System.out.println(">>>>>>>>>>>>>>>>>>>>> Username from principal: " + usernameFromPrincipal);

                User userFromRepo = userRepo.findByEmail(usernameFromPrincipal);
                System.out.println("User fetched from repository: " + userFromRepo);

                return userFromRepo;
            } else {
                System.out.println("Principal is not an instance of UserDetails.");

            }
        }
        return null; // No authenticated user or not UserDetails principal
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean validateUserPassword(User user, String password) {
        String storedHash = user.getPassword(); // Get the stored hash from the database
        return passwordEncoder.matches(password, storedHash);

    }

}
