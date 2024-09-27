package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.model.EmailDetails;
import jakarta.mail.internet.InternetAddress;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendSimpleMail(EmailDetails details)
    {
        try {

            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
