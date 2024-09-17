package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.ProductCategory;
import focodo_ecommerce.backend.entity.embeddedID.ProductCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
}
