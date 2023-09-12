package sg.edu.nus.iss.spring_security_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public CheckoutDetails getOrderDetails() {
        return this.getOrderDetails() ;
    }
}
