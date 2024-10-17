package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private int id;
    private String method;

    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        this.id = paymentMethod.getId();
        this.method = paymentMethod.getMethod();
    }
}
