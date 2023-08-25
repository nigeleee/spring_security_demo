package sg.edu.nus.iss.spring_security_demo.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getOrderId() {
        return orderId.toString();
    }

    public Object getEmail() {
        return customerEmail;
    }

}
