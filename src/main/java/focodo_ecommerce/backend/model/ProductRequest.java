package focodo_ecommerce.backend.model;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private Long original_price;
    private Long sell_price;
    private String sub_description;
    private String main_description;
    @Max(value =  1, message = "DISCOUNT_INVALID")
    private Double discount;
    private int package_quantity;
    private int quantity;
    private List<Integer> categories;
}
