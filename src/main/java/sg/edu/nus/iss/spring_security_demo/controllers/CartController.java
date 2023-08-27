package sg.edu.nus.iss.spring_security_demo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.spring_security_demo.entity.Product;
import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.model.CartItemSummary;
import sg.edu.nus.iss.spring_security_demo.model.OrderDetails;
import sg.edu.nus.iss.spring_security_demo.service.CartItemService;
import sg.edu.nus.iss.spring_security_demo.service.ProductService;
import sg.edu.nus.iss.spring_security_demo.service.UserService;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private HttpSession session;
    @Autowired
    private UserService userService;

    // For authenticated users
    @PostMapping("/user/add")
    public ResponseEntity<String> addUserProductToCart(@RequestParam Long productId, @RequestParam int quantity) {
        User user = userService.getCurrentUser();
        if (user != null) {
            cartItemService.addToUserCart(productId, user, quantity);
            return ResponseEntity.ok("Product added to user's cart");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }

    // For guests
    @PostMapping("/guest/add")
    public ResponseEntity<String> addGuestProductToCart(@RequestParam Long productId, @RequestParam int quantity,
            HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        cartItems.put(productId, quantity);
        session.setAttribute("cartItems", cartItems);
        return ResponseEntity.ok("Product added to guest's cart");
    }

    // @PostMapping("/add-test")
    // public ResponseEntity<String> testAdd(@RequestParam Long productId,
    // @RequestParam int quantity) {

    // cartItemService.testingAdd(productId, quantity);

    // return ResponseEntity.ok("product added");

    // }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemSummary>> getAllCartItems(HttpSession session) {

        User user = userService.getCurrentUser();

        if (user != null) {
            // User is logged in, get user's cart from the database
            List<CartItemSummary> cartItems = cartItemService.getCartItemsForUser(user.getUserId());
            return ResponseEntity.ok(cartItems);
        } else {

            Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");

            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>()); // Return an empty list
            }

            List<CartItemSummary> cartItemSummaries = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                Long productId = entry.getKey();
                int quantity = entry.getValue();

                // Fetch the product from the database by productId
                Product product = productService.getProductById(productId);

                if (product != null) {
                    CartItemSummary summary = new CartItemSummary();
                    summary.setName(product.getName());
                    summary.setTotalPrice(product.getPrice() * quantity);
                    summary.setQuantity(quantity);
                    cartItemSummaries.add(summary);
                }
            }

            return ResponseEntity.ok(cartItemSummaries);
        }

    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartItemId, HttpSession session) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.ok("Cart item removed");
    }

    @PostMapping("/clear-cart")
    public ResponseEntity<String> clearCart(HttpSession session) {
        session.removeAttribute("cartItems");
        return ResponseEntity.ok("Cart cleared");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> completeCheckout(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @ModelAttribute OrderDetails orderDetails) {

        cartItemService.checkoutAndCreateOrder(productId, quantity, orderDetails);

        session.removeAttribute("cartItems");

        return ResponseEntity.ok("Checkout completed");
    }

}

/*
 * @PostMapping("/add")
 * public ResponseEntity<String> addProductToCart(@RequestParam Long
 * productId, @RequestParam int quantity,
 * HttpSession session) {
 * 
 * Product product = productService.getProductById(productId);
 */

// if (product != null) {
// cartItemService.addToCart(productId, quantity);
// return ResponseEntity.ok("Product added to cart");
// } else {
// return ResponseEntity.badRequest().body("Product not found");
// }

// if (product != null) {
// // Retrieve the current cart items from the session
// List<CartItem> cartItems = (List<CartItem>)
// session.getAttribute("cartItems");
// if (cartItems == null) {
// cartItems = new ArrayList<>();
// }

// // Add the new cart item
// CartItem cartItem = new CartItem();
// cartItem.setProduct(product);
// cartItem.setQuantity(quantity);
// cartItems.add(cartItem);

// // Store the updated cart items back to the session
// session.setAttribute("cartItems", cartItems);

// return ResponseEntity.ok("Product added to cart");
// } else {
// return ResponseEntity.badRequest().body("Product not found");
// }

// @GetMapping("/cart")
// public ResponseEntity<List<CartItemSummary>> getAllCartItems() {
// try {
// List<CartItem> cartItems = cartItemService.getAllCartItems();
// List<CartItemSummary> cartItemSummaries = new ArrayList<>();

// for (CartItem cartItem : cartItems) {
// CartItemSummary summary = new CartItemSummary();
// summary.setName(cartItem.getProduct().getName());
// summary.setTotalPrice(cartItem.getProduct().getPrice() *
// cartItem.getQuantity());
// summary.setQuantity(cartItem.getQuantity());
// cartItemSummaries.add(summary);
// }

// return ResponseEntity.ok(cartItemSummaries);

// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
// }

// }

// @DeleteMapping("/delete/{cartItemId}")
// public ResponseEntity<String> removeCartItem(@PathVariable Long cartItemId,
// HttpSession session) {
// // Get the cart items from the session
// List<CartItem> cartItems = (List<CartItem>)
// session.getAttribute("cartItems");

// if (cartItems != null) {
// // Find and remove the cart item from the list
// CartItem cartItemToRemove = null;
// for (CartItem cartItem : cartItems) {
// if (cartItem.getCartId().equals(cartItemId)) {
// cartItemToRemove = cartItem;
// break;
// }
// }

// if (cartItemToRemove != null) {
// cartItems.remove(cartItemToRemove);
// // Update the session with the modified list
// session.setAttribute("cartItems", cartItems);
// return ResponseEntity.ok("Cart item removed");
// }
// }

// return ResponseEntity.badRequest().body("Cart item not found");
// }

// @PostMapping("/add")
// public ResponseEntity<String> addProductToCart(@RequestParam Long productId,
// @RequestParam int quantity, HttpSession session) {

// System.out.println(">>>>>>>>>>>>>>>>>>>>>> in getCurrentUser method");
// User user = userService.getCurrentUser();

// System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Get current user %s%n",
// user);

// if (user != null) {
// // User is logged in, add to user's cart in the database
// cartItemService.addToUserCart( productId, user, quantity);
// System.out.printf(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Details of
// product id, user and quantity added: productId=%s, user=%s, quantity=%s%n",
// productId, user, quantity);
// return ResponseEntity.ok("Product added to user's cart");

// } else {

// // Retrieve the map of product IDs to quantities from the session
// Map<Long, Integer> cartItems = (Map<Long, Integer>)
// session.getAttribute("cartItems");

// if (cartItems == null) {
// cartItems = new HashMap<>();
// }

// // Add the product ID and quantity to the map
// cartItems.put(productId, quantity);

// // Update the map in the session
// session.setAttribute("cartItems", cartItems);

// return ResponseEntity.ok("Product added to cart");
// }
// }