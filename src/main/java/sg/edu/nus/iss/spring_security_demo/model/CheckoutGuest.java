package sg.edu.nus.iss.spring_security_demo.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutGuest {
    private List<CheckoutDetails> cartItems;
    private OrderDetails orderDetails;
}
