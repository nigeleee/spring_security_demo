package sg.edu.nus.iss.spring_security_demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.spring_security_demo.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

}
