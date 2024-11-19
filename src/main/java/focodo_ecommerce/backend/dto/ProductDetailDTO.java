package focodo_ecommerce.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.ProductImage;
import focodo_ecommerce.backend.model.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailDTO {
    private int id;
    private String name;
    private Long original_price;
    private Long sell_price;
    private String sub_description;
    private String main_description;
    private Double discount;
    private int package_quantity;
    private int sold_quantity;
    private int quantity;
    private ReviewResponse review;
    private List<CategoryDTO> categories;
    private List<String> images;
    private boolean is_delete;

    public ProductDetailDTO(Product saveProduct) {
        this.id = saveProduct.getId();
        this.name = saveProduct.getName();
        this.original_price = saveProduct.getOriginal_price();
        this.sell_price = saveProduct.getSell_price();
        this.sub_description = saveProduct.getSub_description();
        this.main_description = saveProduct.getMain_description();
        this.discount = saveProduct.getDiscount();
        this.sold_quantity = saveProduct.getSold_quantity();
        this.package_quantity = saveProduct.getPackage_quantity();
        this.quantity = saveProduct.getQuantity();
        this.categories = (saveProduct.getProductCategories() != null) ? saveProduct.getProductCategories().stream().map(productCategory -> new CategoryDTO(productCategory.getCategory())).toList() : null;
        this.images = (saveProduct.getProductImageList() != null) ? saveProduct.getProductImageList().stream().map(ProductImage::getImage).toList() : List.of();
        this.review = (saveProduct.getReviews() != null) ? new ReviewResponse(saveProduct.getReviews()) : null;
        this.is_delete = saveProduct.is_delete();
    }
}
