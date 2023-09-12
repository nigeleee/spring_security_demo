package sg.edu.nus.iss.spring_security_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="cart_item")
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;
    // private Long productId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;
    private Integer quantity;

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getProductId() {
        return product.getProductId();
    }
    
}
