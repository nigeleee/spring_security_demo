// package sg.edu.nus.iss.spring_security_demo.model;

// import lombok.Data;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import sg.edu.nus.iss.spring_security_demo.entity.User;

// import java.util.Collection;
// import java.util.Collections;

// @Data
// public class CustomUserDetails implements UserDetails {

//     private final User user;

//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         // Implement this method based on your user's roles/permissions
//         // Return a collection of GrantedAuthority objects
//         return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
//     }

//     @Override
//     public String getPassword() {
//         return user.getPassword();
//     }

//     @Override
//     public String getUsername() {
//         return user.getEmail();
//     }

//     @Override
//     public boolean isAccountNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isAccountNonLocked() {
//         return true;
//     }

//     @Override
//     public boolean isCredentialsNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isEnabled() {
//         return user.isEnabled();
//     }
// }

