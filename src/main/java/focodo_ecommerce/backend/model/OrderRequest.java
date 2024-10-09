package focodo_ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String description;
    private Long shipping_price;
    private Long total_price;
    private Long discount_price;
    private Long final_price;
    private int payment_method;
    private String id_voucher;
    private List<OrderDetailRequest> details;
}
