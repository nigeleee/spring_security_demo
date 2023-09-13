package sg.edu.nus.iss.spring_security_demo.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="user")
public class User {

    public User() {
    }
    // declare userId as primary key
    @Id
    // indicate primary key is generated by database, using auto increment
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String password;

    @Column(unique = true) // Enforce unique email
    private String email;
    private String role;
    private boolean enabled = false;
    
    // @Column(name = "is_profile_complete")
    // private boolean isProfileComplete = false;
        
}
