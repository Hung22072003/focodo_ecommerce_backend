package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.Notification;
import focodo_ecommerce.backend.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private int id;
    private String content;
    private boolean is_read;
    private LocalDateTime create_time;
    private String type;
    private OrderDTO order;

    public NotificationDTO(Notification foundNotification) {
        this.id = foundNotification.getId();
        this.content = foundNotification.getContent();
        this.is_read = foundNotification.is_read();
        this.create_time = foundNotification.getCreateTime();
        this.type = foundNotification.getType();
        this.order = new OrderDTO(foundNotification.getOrder());
    }
}
