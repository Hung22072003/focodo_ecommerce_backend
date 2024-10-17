package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    private int id;
    private String status;

    public OrderStatusDTO(OrderStatus orderStatus) {
        this.id = orderStatus.getId();
        this.status = orderStatus.getStatus();
    }
}
