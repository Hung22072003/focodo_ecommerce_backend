package focodo_ecommerce.backend.entity.embeddedID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ProductCategoryId implements Serializable {
    @Column(name = "id_product")
    private int id_product;

    @Column(name = "id_category")
    private int id_category;

    public ProductCategoryId() {
    }

    public ProductCategoryId(int id_product, int id_category) {
        this.id_product = id_product;
        this.id_category = id_category;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ProductCategoryId that)) return false;
        return getId_product() == that.getId_product() && getId_category() == that.getId_category();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_product(), getId_category());
    }
}
