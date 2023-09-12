package sg.edu.nus.iss.spring_security_demo.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.spring_security_demo.entity.CartItem;
import sg.edu.nus.iss.spring_security_demo.entity.Product;
import sg.edu.nus.iss.spring_security_demo.entity.User;
import sg.edu.nus.iss.spring_security_demo.model.CartItemSummary;
import sg.edu.nus.iss.spring_security_demo.model.CheckoutGuest;
import sg.edu.nus.iss.spring_security_demo.model.CheckoutDetails;
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

    @GetMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> result = productService.getAllProducts();

        if (result != null) {
            return ResponseEntity.ok().body(result);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping(path = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product result = productService.getProductById(id);
            return ResponseEntity.ok().body(result);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProductToCart(@RequestParam Long productId, @RequestParam int quantity,
            HttpSession session) {

        System.out.println(
                ">>>>>>>>>>>>>>>>>>>> Inside addProductToCart: productId = " + productId + ", quantity = " + quantity);

        User user = userService.getCurrentUser();
        System.out.println(">>>>>>>>>>>>>>>>>>>>. User:" + user);

        if (user != null) {
            // Add to user's permanent cart
            cartItemService.addToUserCart(productId, user, quantity);
            System.out.println(
                    ">>>>>>>>>>>>>>>>>>>>> Products added for User:" + user + "added" + quantity + " of " + productId);
            return ResponseEntity.ok("{\"message\":\"Product added to user's cart\"}");
        } else {
            // Add to guest's session cart
            Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new HashMap<>();
            }
            int existingQuantity = cartItems.getOrDefault(productId, 0);
            cartItems.put(productId, existingQuantity + quantity);
            session.setAttribute("cartItems", cartItems);
            System.out.println("Session ID in addProductToCart: " + session.getId());

            return ResponseEntity.ok("{\"message\":\"Product added to guest's cart\"}");

        }
    }

    // // For authenticated users
    // @PostMapping(path="/user/add", consumes=MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> addUserProductToCart(@RequestParam Long
    // productId, @RequestParam int quantity) {
    // User user = userService.getCurrentUser();
    // if (user != null) {
    // cartItemService.addToUserCart(productId, user, quantity);
    // return ResponseEntity.ok("{\"message\":\"Product added to cart\"}");
    // } else {
    // return
    // ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"User not
    // authenticated\"}");
    // }
    // }

    // // For guests
    // @PostMapping("/guest/add")
    // public ResponseEntity<String> addGuestProductToCart(@RequestParam Long
    // productId, @RequestParam int quantity,
    // HttpSession session) {
    // Map<Long, Integer> cartItems = (Map<Long, Integer>)
    // session.getAttribute("cartItems");
    // if (cartItems == null) {
    // cartItems = new HashMap<>();
    // }
    // cartItems.put(productId, quantity);
    // session.setAttribute("cartItems", cartItems);
    // return ResponseEntity.ok("{\"message\":\"Product added to guest's cart\"}");
    // }

    // @PostMapping("/add-test")
    // public ResponseEntity<String> testAdd(@RequestParam Long productId,
    // @RequestParam int quantity) {

    // cartItemService.testingAdd(productId, quantity);

    // return ResponseEntity.ok("product added");

    // }

    @GetMapping(path = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartItemSummary>> getAllCartItems(HttpSession session) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>> Inside getAllCartItems");

        User user = userService.getCurrentUser();
        System.out.println(">>>>>>>>>>>>>>>>>>>> current User: " + user);

        if (user != null) {
            // User is logged in, get user's cart from the database
            List<CartItemSummary> cartItems = cartItemService.getCartItemsForUser(user.getUserId());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>CartItems For Logged In User: " + cartItems);

            return ResponseEntity.ok(cartItems);
        } else {

            Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");

            System.out.println("Session ID: " + session.getId());

            if (cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>()); // Return an empty list
            }

            List<CartItemSummary> cartItemSummaries = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
                Long productId = entry.getKey();
                int quantity = entry.getValue();

                System.out.println(
                        ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Product ID: " + productId + " Quantity: " + quantity);

                // Fetch the product from the database by productId
                Product product = productService.getProductById(productId);

                System.out.println("Product: " + product);

                if (product != null) {
                    CartItemSummary summary = new CartItemSummary();
                    summary.setName(product.getName());
                    summary.setTotalPrice(product.getPrice() * quantity);
                    summary.setQuantity(quantity);
                    summary.setProductId(product.getProductId());
                    cartItemSummaries.add(summary);
                }
            }

            return ResponseEntity.ok(cartItemSummaries);
        }

    }

    // delete for guest
    @DeleteMapping("/cart/delete/guest/{productId}")
    public ResponseEntity<String> removeGuestCartItem(HttpSession session, @PathVariable Long productId) {
        // Retrieve cart items from the session
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute("cartItems");

        System.out.println(">>>>>>>>>>>>> Cart Items: " + cartItems);

        if (cartItems != null && cartItems.containsKey(productId)) {
            // Remove the item from the cart map

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>Removed product with ID: " + productId);

            cartItems.remove(productId);
            // Update the cart in the session
            session.setAttribute("cartItems", cartItems);

            return ResponseEntity.ok("{\"message\":\"Cart item removed\"}");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Product not found in cart\"}");
        }
    }

    // delete for user
    @DeleteMapping("/cart/delete/user/{cartId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartId) {
        cartItemService.removeUserCartItem(cartId);
        return ResponseEntity.ok("{\"message\":\"Cart item removed\"}");
    }

    // @PostMapping("/cart/clear/guest-cart")
    // public ResponseEntity<String> clearCart(HttpSession session) {
    // session.removeAttribute("cartItems");
    // return ResponseEntity.ok("{\"message\":\"Cart Cleared\"}");
    // }
    @PostMapping("/cart/clear-guest")
    public ResponseEntity<String> clearCart(HttpSession session) {
        session.removeAttribute("cartItems");
        return ResponseEntity.ok("{\"message\":\"Guest cart cleared\"}");
    }

    @PostMapping("/cart/clear-user")
    public ResponseEntity<String> clearUserCart() {
        User user = userService.getCurrentUser();

        cartItemService.clearUserCart(user.getUserId());

        return ResponseEntity.ok("{\"message\":\"User cart cleared\"}");
    }

    @PostMapping("/checkout/user")
    public ResponseEntity<String> userCheckout(@RequestBody List<CheckoutDetails> checkoutItems) {

        try {
            // Long productId = request.getProductId();
            // int quantity = request.getQuantity();

            User user = userService.getCurrentUser();

            // System.out.println(">>>>>>>>>>>>>>>>>>>> Product id, Quantity, User details "
            // + productId + quantity + user);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"User not authenticated\"}");
            }

            cartItemService.createOrderForUser(checkoutItems, user);
            System.out.println("Cart items and user details: " + checkoutItems + user);
            return ResponseEntity.ok("{\"message\":\"User Checkout Completed\"}");

        } catch (Exception e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"An error occurred during checkout\"}");
        }
    }

    @PostMapping(path = "/checkout/guest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> guestCheckout(@RequestBody CheckoutGuest checkoutItems) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> inside guest cart controller");
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> checkout items: " + checkoutItems);
            cartItemService.createOrderForGuest(checkoutItems.getCartItems(), checkoutItems.getOrderDetails());
            return new ResponseEntity<>("{\"message\":\"Guest Checkout Completed\"}", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            return new ResponseEntity<>("{\"message\":\"An error occurred during checkout\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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