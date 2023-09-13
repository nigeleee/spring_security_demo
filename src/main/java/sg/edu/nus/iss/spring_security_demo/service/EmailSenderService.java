package sg.edu.nus.iss.spring_security_demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import sg.edu.nus.iss.spring_security_demo.entity.CartItem;
import sg.edu.nus.iss.spring_security_demo.entity.Order;
import sg.edu.nus.iss.spring_security_demo.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    // @Value("${MAIL_USERNAME}")
    // private String email;
    private static final String email = "nigeleee@gmail.com";

    public void sendSimpleEmail(String toEmail, String body, String subject) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();

        // message.setFrom("nigeleee@gmail.com");
        message.setFrom(email);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail sent");
    }

    public void sendHtmlEmail(String toEmail, String body, String subject) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // helper.setFrom("nigeleee@gmail.com");
        helper.setFrom(email);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true); // Set HTML content

        mailSender.send(message);

        System.out.println("HTML email sent");
    }

    // public void sendReceiptEmail(Order order, String toEmail) {
    //     StringBuilder receiptBuilder = new StringBuilder();
    //     receiptBuilder.append("Thank you for your purchase!\n");
    //     receiptBuilder.append("Order ID: " + order.getOrderId() + "\n");

    //     try {
    //         sendSimpleEmail(toEmail, receiptBuilder.toString(), "Your Receipt");
    //     } catch (MessagingException e) {
    //         log.error("Error sending receipt to email: {}", order.getEmail(), e);
    //     }

    // }

    public void sendReceiptEmail(Order order, String applicationUrl) throws MessagingException {
    StringBuilder sb = new StringBuilder();
    sb.append("<h1>Order Receipt</h1>")
      .append("<h3>Thank you for your purchase</h3>")
      .append("<p>Order ID: ").append(order.getOrderId()).append("</p>")
      .append("<p>Name: ").append(order.getFirstName()).append(" ").append(order.getLastName()).append("</p>")
      .append("<p>Email: ").append(order.getEmail()).append("</p>")
      .append("<p>Phone: ").append(order.getPhone()).append("</p>")
      .append("<p>Address: ").append(order.getAddress()).append("</p>");

    sb.append("<h2>Items:</h2><ul>");
    for (CartItem item : order.getCartItems()) {
        sb.append("<li>").append(item.getProduct().getName())
          .append(" (Quantity: ").append(item.getQuantity())
          .append(", Price: ").append(item.getProduct().getPrice()).append(")</li>");
    }
    sb.append("</ul>");

    String subject = "Your Order Receipt";
    sendHtmlEmail((String) order.getEmail(), sb.toString(), subject);
}


    public void sendVerificationEmail(User user, String token, String applicationUrl) throws MessagingException {
        String url = applicationUrl + "/api/verifyRegistration?token=" + token;
        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h2>Welcome to our website!</h2>" +
                "        <p>Thank you for registering. Please click the button below to verify your account:</p>" +
                "        <a class=\"btn\" href=\"" + url + "\">Verify Account</a>" +
                "        <p>If the button doesn't work, you can also copy and paste the following link into your browser:</p>"
                +
                "        <p><a href=\"" + url + "\">" + url + "</a></p>" +
                "        <p>If you did not request this registration, you can safely ignore this email.</p>" +
                "        <p>Best regards,<br>Your Website Team</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
        String subject = "Account Verification";

        sendHtmlEmail(user.getEmail(), body, subject);
    }

}
