package focodo_ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    private String image;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ProductCategory> products;

    @ManyToOne
    @JoinColumn(name = "parent_category")
    private Category parent_category;

    @OneToMany(mappedBy = "parent_category", cascade = CascadeType.ALL)
    private List<Category> subcategories;
}
