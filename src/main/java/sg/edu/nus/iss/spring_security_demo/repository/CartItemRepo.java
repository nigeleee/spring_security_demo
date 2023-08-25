package sg.edu.nus.iss.spring_security_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.spring_security_demo.entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long>{

    void deleteById(Long cartItemId);

    List<CartItem> findByUserUserId(Long userId);
    
}
