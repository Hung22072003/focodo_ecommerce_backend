package focodo_ecommerce.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import focodo_ecommerce.backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private String id_order;
    private String description;
    private Long shipping_price;
    private Long total_price;
    private Long discount_price;
    private Long final_price;
    private LocalDateTime order_date;
    private String order_status;
    private String payment_status;
    private String payment_method;
    private boolean review_check;
    private UserDTO customer;
    List<OrderDetailDTO> order_details;

    public OrderDTO(Order order) {
        this.id_order = order.getId_order();
        this.description = order.getDescription();
        this.shipping_price = order.getShipping_price();
        this.total_price = order.getTotal_price();
        this.discount_price = order.getDiscount_price();
        this.final_price = order.getFinal_price();
        this.order_date = order.getOrder_date();
        this.order_status = order.getOrderStatus().getStatus();
        this.payment_status = order.getPaymentStatus().getStatus();
        this.review_check = order.is_check();
        this.payment_method = order.getPaymentMethod().getMethod();
        this.customer = order.getUser() != null ? new UserDTO(order.getUser()) : null;
        this.order_details = order.getOrderDetails() != null ? order.getOrderDetails().stream().map(OrderDetailDTO::new).toList() : List.of();
    }
}
