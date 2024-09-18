package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.ProductImage;
import jakarta.validation.constraints.Max;
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
    private String sub_description;
    private String main_description;
    private Double discount;
    private int package_quantity;
    private int quantity;
    private List<String> images;

    public ProductDTO(Product saveProduct) {
        this.id = saveProduct.getId();
        this.name = saveProduct.getName();
        this.original_price = saveProduct.getOriginal_price();
        this.sell_price = saveProduct.getSell_price();
        this.sub_description = saveProduct.getSub_description();
        this.main_description = saveProduct.getMain_description();
        this.discount = saveProduct.getDiscount();
        this.package_quantity = saveProduct.getPackage_quantity();
        this.quantity = saveProduct.getQuantity();
        this.images = (saveProduct.getProductImageList() != null) ? saveProduct.getProductImageList().stream().map(ProductImage::getImage).toList() : List.of();
    }
}