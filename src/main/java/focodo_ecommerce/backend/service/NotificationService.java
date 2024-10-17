package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.NotificationDTO;
import focodo_ecommerce.backend.model.PaginationObjectResponse;

public interface NotificationService {
    public NotificationDTO createNotification(String id_order);

    PaginationObjectResponse getAllNotifications(int page, int size);

    void updateNotification(int id);
}
