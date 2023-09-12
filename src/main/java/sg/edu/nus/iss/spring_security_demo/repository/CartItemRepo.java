package sg.edu.nus.iss.spring_security_demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.spring_security_demo.entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    void deleteById(Long cartId);

    List<CartItem> findByUserUserId(Long userId);

    Optional<CartItem> findByProductProductIdAndUserUserId(Long productId, Long userId);

    // @Query("SELECT c FROM CartItem c WHERE c.product.id = :productId AND c.user.userId = :userId")
    // Optional<CartItem> findByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);

}
