package focodo_ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestBody {
    private CustomerRequest customer;
    private OrderRequest order;
}
