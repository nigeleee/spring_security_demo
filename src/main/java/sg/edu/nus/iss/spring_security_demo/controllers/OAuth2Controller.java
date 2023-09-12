package sg.edu.nus.iss.spring_security_demo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api")
public class OAuth2Controller {

    // @Value("${redirect.url}")
    // private String redirectUrl;

    @GetMapping("/success")
    public ResponseEntity<?> oauth2Redirect(HttpServletResponse httpServletResponse) {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>> Inside Success handler");
            httpServletResponse.sendRedirect("http://localhost:4200/products");
            // httpServletResponse.sendRedirect("https://miniprojectdemo-production.up.railway.app/products");
            // System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> Redirecting to: " + redirectUrl);
            // httpServletResponse.sendRedirect(redirectUrl);
            

        } catch (IOException e) {
            System.err.println("Error during redirect: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
            
        }
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/logout/oauth2")
    public ResponseEntity<String> logoutOAuth(HttpServletRequest request) {

        try {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> In logout method oauth2");
        
        // Invalidate the HTTP session and clear the SecurityContext
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        
        return ResponseEntity.ok("{\"message\":\"Logged Out\"}");
        } catch (Exception e) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> Error in logout: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    }

    // @RequestMapping("/success")
    // public void redirect(HttpServletResponse httpServletResponse) throws
    // IOException {
    // httpServletResponse.sendRedirect("http://localhost:4200/products");
    // }

    // @RequestMapping("/failure")
    // public void failureRedirect(HttpServletResponse httpServletResponse) throws
    // IOException {
    // // Redirect to your Angular app's login page with an error query parameter.
    // // Your Angular app should look for this parameter and display an error
    // message if it exists.
    // httpServletResponse.sendRedirect("http://localhost:4200/login?error");
    // }

