package sg.edu.nus.iss.spring_security_demo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.event.RegistrationCompleteEvent;
import sg.edu.nus.iss.spring_security_demo.model.UserModel;
import sg.edu.nus.iss.spring_security_demo.service.UserService;

@RestController
// @RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        
        return "Success"; 
    } 

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificaionToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }

        return "Bad User";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    // @PostMapping("/register")
    // public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
    //     User newUser = new User();
    //     newUser.setUsername(registrationRequest.getUsername());
    //     newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
    //     newUser.setEmail(registrationRequest.getEmail());
    //     // Set roles and other properties if needed
    //     userRepo.save(newUser);

    

    //     return ResponseEntity.ok("User registered successfully");
    // }

    // @PostMapping("/login")
    // public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest loginRequest) {
    //     try {
    //         Authentication authentication = authenticationManager.authenticate(
    //                 new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    //         UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

    //         // Create JWT token if needed and return it in the response

    //         return ResponseEntity.ok("Login successful");
    //     } catch (UsernameNotFoundException ex) {
    //         return ResponseEntity.badRequest().body("User not found");
    //     } catch (Exception ex) {
    //         return ResponseEntity.status(401).body("Invalid credentials");
    //     }
    // }

}
