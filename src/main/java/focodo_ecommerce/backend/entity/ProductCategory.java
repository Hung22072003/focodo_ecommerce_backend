package focodo_ecommerce.backend.entity;

import focodo_ecommerce.backend.entity.embeddedID.ProductCategoryId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @EmbeddedId
    private ProductCategoryId productCategoryId;

    @ManyToOne
    @MapsId("id_product")
    @JoinColumn(name = "id_product")
    private Product product;
    @ManyToOne
    @MapsId("id_category")
    @JoinColumn(name = "id_category")
    private Category category;
}
