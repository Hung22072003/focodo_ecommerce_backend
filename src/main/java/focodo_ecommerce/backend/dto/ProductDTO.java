package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.Review;
import focodo_ecommerce.backend.model.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private int id;
    private String name;
    private Long original_price;
    private Long sell_price;
    private Double discount;
    private int quantity;
    private int sold_quantity;
    private double review;
    private String image;
    private boolean is_delete;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.original_price = product.getOriginal_price();
        this.sell_price = product.getSell_price();
        this.quantity = product.getQuantity();
        this.sold_quantity = product.getSold_quantity();
        this.discount = product.getDiscount();
        this.is_delete = product.is_delete();
        this.review = (double) product.getReviews().stream().map(Review::getRating).reduce(0, Integer::sum) / product.getReviews().size();
        this.image = (!product.getProductImageList().isEmpty()) ? product.getProductImageList().get(0).getImage() : null;
    }
}
