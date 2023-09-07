package sg.edu.nus.iss.spring_security_demo.controllers;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.event.RegistrationCompleteEvent;
import sg.edu.nus.iss.spring_security_demo.model.AuthenticationResponse;
import sg.edu.nus.iss.spring_security_demo.model.AuthenticationRequest;
import sg.edu.nus.iss.spring_security_demo.model.UserModel;
import sg.edu.nus.iss.spring_security_demo.service.JwtBlacklistService;
import sg.edu.nus.iss.spring_security_demo.service.UserDetailsSvc;
import sg.edu.nus.iss.spring_security_demo.service.UserService;
import sg.edu.nus.iss.spring_security_demo.util.JwtUtil;

@RestController
@RequestMapping("/api")
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
    @Autowired
    private JwtBlacklistService blacklistService;

    @PostMapping(path="/register", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) throws Exception{
        
        try {
           User user = userService.registerUser(userModel);

            System.out.println(">>>>>>>>>>>>>>>>>> user details are:" + user);

            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request))); 

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Email exists\"}");
        }
        
        return ResponseEntity.ok("{\"message\":\"Registered Successfully\"}");
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

    // traditional login
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        UserDetails userDetails = null;
        try {
            userDetails = userDetailsSvc.loadUserByUsername(authenticationRequest.getEmail());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Email does not exist\"}");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Incorrect password\"}");
        }

        // final UserDetails userDetails = userDetailsSvc.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    // traditional logout
    @PostMapping("/logout")
    public ResponseEntity<String> logoutJwt(HttpServletRequest request) {

        System.out.println(">>>>>>>>>>>>>>>>>>>> in logout method traditional");

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Assuming you can decode the token to get its expiration time
            long expirationTimeMillis = jwtUtil.getExpirationTimeMillisFromToken(token); // Retrieve from the token
            blacklistService.addToBlacklist(token, expirationTimeMillis);
        }

        new SecurityContextLogoutHandler().logout(request, null, null);

        // JsonObject json = Json.createObjectBuilder()
        // .add("status", "success")
        // .add("message", "Logged Out")
        // .build();

        // return ResponseEntity.ok(json.toString());
        return ResponseEntity.ok("{\"message\":\"Logged Out\"}");

    }

    // use angular to blacklist token on client side
    // @PostMapping(path="/logout")
    // public ResponseEntity<String> logout(HttpServletRequest request,
    // HttpServletResponse response) {
    // new SecurityContextLogoutHandler().logout(request, response, null);
    // return ResponseEntity.ok("Logged Out");
    // }

}
