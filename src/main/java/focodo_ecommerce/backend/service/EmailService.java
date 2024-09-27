package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.model.EmailDetails;

public interface EmailService {
    void sendSimpleMail(EmailDetails details);
}
