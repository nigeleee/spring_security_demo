package sg.edu.nus.iss.spring_security_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartItemSummary {
    private String name;
    private double totalPrice;
    private int quantity;
    private Long cartId;
    private Long userId;
    private Long orderId;
    private Long productId;

    public void setProducId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String name) {
        this.name = name;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
