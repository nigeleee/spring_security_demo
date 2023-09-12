package sg.edu.nus.iss.spring_security_demo.entity;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class Order {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private Long orderId;
    private Long orderId;

    @OneToMany(mappedBy = "order")
    private List<CartItem> cartItems;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderId() {
        return orderId.toString();
    }

    public Object getEmail() {
        return email;
    }

}
