package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private int id_cart;
    private int id_product;
    private String product_name;
    private int quantity;
    private Long unit_price;
    private Long original_price;
    private String image;
    private Boolean check;
    public CartDTO(Cart cart) {
        this.id_cart = cart.getId_cart();
        this.id_product = cart.getProduct().getId();
        this.product_name = cart.getProduct().getName();
        this.quantity = cart.getQuantity();
        this.unit_price = cart.getProduct().getSell_price();
        this.original_price = cart.getProduct().getOriginal_price();
        this.image = cart.getProduct().getProductImageList().get(0).getImage();
        this.check = cart.getCheck();
    }
}
