package sg.edu.nus.iss.spring_security_demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import sg.edu.nus.iss.spring_security_demo.service.UserDetailsSvc;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserDetailsSvc userDetailsSvc;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsSvc).passwordEncoder(passwordEncoder());
    }

    private static final String[] WHITE_LIST_URLS = {
            "/hello",
            "/api/login",
            "/api/register",
            "/api/verifyRegistration",
            "/api/guest/add", 
            "/api/logout",
            "/api/success",
            "/api/oauth2/authorization/google"

    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
             http
                
                .authorizeHttpRequests() 
                .requestMatchers(WHITE_LIST_URLS).permitAll() //anyone can access
                .requestMatchers("/api/user/**").hasRole("USER")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() //must be authenticated
                .and()
                // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                // .defaultSuccessUrl("/success", true)
                .defaultSuccessUrl("/success", true)
                .and()
                .cors()
                .and()
                .csrf()
                .disable();

            return http.build();
    }
      @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsSvc);
        return provider;
    }

    @Bean
    public LogoutSuccessHandler oidcLogoutSuccessHandler() {
    return (request, response, authentication) -> {
        // Your custom logic to handle local logout.
        // For example, you can revoke tokens here, log an event, etc.

        // Redirecting to a specific URL after logout.
        response.sendRedirect("http://localhost:4200/login?logout");
    };
}

}