package sg.edu.nus.iss.spring_security_demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.spring_security_demo.entity.CartItem;
import sg.edu.nus.iss.spring_security_demo.entity.Order;
import sg.edu.nus.iss.spring_security_demo.entity.Product;
import sg.edu.nus.iss.spring_security_demo.model.OrderDetails;
import sg.edu.nus.iss.spring_security_demo.repository.CartItemRepo;
import sg.edu.nus.iss.spring_security_demo.repository.OrderRepo;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderService orderService;

    public void addToCart(Long productId, Integer quantity) {

        // System.out.println("----------------------------------------->Adding product with productId: " + productId + " to cart with quantity: " + quantity);

        // Create a new cart item
        Product product = productService.getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        // Save the cart item
        cartItemRepo.save(cartItem);

        // System.out.println("------------------------------------------>CartItem saved: " + cartItem.getCartId()); // Print saved cartItem

        
    }
   
    public List<CartItem> getAllCartItems() {
        return cartItemRepo.findAll();
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }

    public void checkoutAndCreateOrder(Long productId, int quantity, OrderDetails orderDetails) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            Order order = new Order();
            order.setCustomerName(orderDetails.getCustomerName());
            order.setCustomerEmail(orderDetails.getCustomerEmail());
            order.setCustomerPhone(orderDetails.getCustomerPhone());
            order.setCustomerAddress(orderDetails.getCustomerAddress());

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            order.setOrderId(new Random().nextLong(100000));
            order.setCartItems(Collections.singletonList(cartItem));

            orderRepo.save(order);
            //send email
            orderService.sendReceiptEmail(order, order.getEmail().toString());
            // Update inventory
            productService.updateInventory(productId, -quantity);
        }
    }
    
}
