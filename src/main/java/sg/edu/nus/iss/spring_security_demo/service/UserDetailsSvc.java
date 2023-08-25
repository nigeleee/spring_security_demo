package sg.edu.nus.iss.spring_security_demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.repository.UserRepo;

@Service
public class UserDetailsSvc implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    // @Override
    // public UserDetails loadUserByUsername(String email) throws
    // UsernameNotFoundException {

    // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>. in loadUserByUserName
    // method");
    // User user = userRepo.findByEmail(email);
    // System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>> User email as user name is: %s%n
    // ", user);
    // if (user == null) {
    // throw new UsernameNotFoundException("User not found");
    // }

    // return new CustomUserDetails(user);
    // }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

}
