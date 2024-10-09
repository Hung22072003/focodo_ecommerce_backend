package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private int id_order_detail;
    private ProductDTO product;
    private int quantity;
    private Long unit_price;
    private Long total_price;

    public OrderDetailDTO(OrderDetail orderDetail) {
        this.id_order_detail = orderDetail.getId_order_detail();
        this.quantity = orderDetail.getQuantity();
        this.unit_price = orderDetail.getUnit_price();
        this.total_price = orderDetail.getTotal_price();
        this.product = new ProductDTO(orderDetail.getProduct());
    }
}