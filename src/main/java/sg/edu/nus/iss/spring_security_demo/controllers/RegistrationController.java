package sg.edu.nus.iss.spring_security_demo.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.event.RegistrationCompleteEvent;
import sg.edu.nus.iss.spring_security_demo.model.AuthenticationResponse;
import sg.edu.nus.iss.spring_security_demo.model.AuthenticationRequest;
import sg.edu.nus.iss.spring_security_demo.model.UserModel;
import sg.edu.nus.iss.spring_security_demo.service.UserDetailsSvc;
import sg.edu.nus.iss.spring_security_demo.service.UserService;
import sg.edu.nus.iss.spring_security_demo.util.JwtUtil;

@RestController
@RequestMapping
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsSvc userDetailsSvc;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));

        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificaionToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }

        return "Bad User";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password");
        }

        final UserDetails userDetails = userDetailsSvc.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    // use angular to blacklist token on client side
    @PostMapping(path="/signout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        new SecurityContextLogoutHandler().logout(request, null, null);
        return ResponseEntity.ok("Logged Out");
    }

}    
    

    // @GetMapping("/login")
    // public String showLoginForm() {
    // return "login"; // Return the name of your login HTML file (e.g.,
    // "login.html")
    // }

    // @PostMapping("/signin")
    // public String processLogin(@RequestParam String email, @RequestParam String
    // password, HttpSession session) {

    // System.out.printf("-------------------------------------------------->Received
    // login request for email: %s%n", email);

    // User user = userService.getUserByEmail(email);

    // if (user != null && userService.validateUserPassword(user, password)) {
    // // Authentication successful
    // // You can perform further actions here, such as setting session attributes
    // or redirecting to a dashboard page
    // System.out.printf("----------------------------------------->User %s
    // successfully logged in %n", email);
    // session.setAttribute("user", user);

    // return "success"; // Redirect to the dashboard page
    // } else {
    // System.out.printf("---------------------------------------->Login failed for
    // user %s%n", email);
    // // Authentication failed
    // // You can show an error message on the login page
    // return "login again"; // Return to the login page
    // }
    // }

    


