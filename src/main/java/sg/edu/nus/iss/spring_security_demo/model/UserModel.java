package sg.edu.nus.iss.spring_security_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserModel {
    
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String role;
    private boolean matchingPassword;
        
}
