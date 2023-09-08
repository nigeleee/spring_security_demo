package sg.edu.nus.iss.spring_security_demo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, 
                                        HttpServletResponse httpServletResponse, 
                                        Authentication authentication) 
                                        throws IOException, ServletException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>> Inside Custom Success handler");
        HttpSession session =httpServletRequest.getSession();
        session.setAttribute("OAUTH2_AUTHENTICATION", "SUCCESS");
        // httpServletResponse.sendRedirect("https://miniprojectdemo-production.up.railway.app/products");
        httpServletResponse.sendRedirect("https://miniprojectdemo-production.up.railway.app/#/products");
        
    }
}
