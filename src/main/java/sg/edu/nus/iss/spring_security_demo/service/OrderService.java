// package sg.edu.nus.iss.spring_security_demo.service;

// import jakarta.mail.MessagingException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import lombok.extern.slf4j.Slf4j;
// import sg.edu.nus.iss.spring_security_demo.entity.Order;

// @Slf4j
// @Service
// public class OrderService {

//     @Autowired
//     private EmailSenderService emailSenderService;
    
//     public void sendReceiptEmail(Order order, String toEmail) {
//         StringBuilder receiptBuilder = new StringBuilder();
//         receiptBuilder.append("Thank you for your purchase!\n");
//         receiptBuilder.append("Order ID: " + order.getOrderId() + "\n");

//         try {
//             emailSenderService.sendSimpleEmail(toEmail, receiptBuilder.toString(), "Your Receipt");
//         } catch (MessagingException e) {
//             log.error("Error sending receipt to email: {}", order.getEmail(), e);
//         }
        
//     }
// }
