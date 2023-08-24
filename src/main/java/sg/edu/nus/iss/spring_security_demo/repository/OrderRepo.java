package sg.edu.nus.iss.spring_security_demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.spring_security_demo.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
    
}
