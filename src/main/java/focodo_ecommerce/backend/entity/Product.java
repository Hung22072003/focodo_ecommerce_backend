package focodo_ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Long original_price;
    private Long sell_price;
    @Column(name = "sub_description", length = 5000)
    private String sub_description;
    @Column(name = "main_description", length = 5000)
    private String main_description;
    private Double discount;
    private int sold_quantity;
    private int package_quantity;
    private int quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductCategory> productCategories;

    @OneToMany(mappedBy = "product", cascade =  CascadeType.ALL)
    private List<ProductImage> productImageList;
}
