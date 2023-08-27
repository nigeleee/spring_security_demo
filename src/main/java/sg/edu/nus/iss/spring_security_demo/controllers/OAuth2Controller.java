package sg.edu.nus.iss.spring_security_demo.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class OAuth2Controller {
    
    //   @RequestMapping("/success")
    // public void redirect(HttpServletResponse httpServletResponse) throws IOException {
    //     httpServletResponse.sendRedirect("http://localhost:4200/products");
    // }

    @GetMapping("/success")
    public ResponseEntity<?> oauth2Redirect(HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.sendRedirect("http://localhost:4200/products");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    // @RequestMapping("/success")
    // public void redirect(HttpServletResponse httpServletResponse) throws IOException {
    //     httpServletResponse.sendRedirect("http://localhost:4200/products");
    // }

    // @RequestMapping("/failure")
    // public void failureRedirect(HttpServletResponse httpServletResponse) throws IOException {
    //     // Redirect to your Angular app's login page with an error query parameter.
    //     // Your Angular app should look for this parameter and display an error message if it exists.
    //     httpServletResponse.sendRedirect("http://localhost:4200/login?error");
    // }
}
