package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);
    @Query(value = "select p from Product p inner join ProductCategory pc on p.id = pc.productCategoryId.id_product where pc.productCategoryId.id_category = :id_category")
    Page<Product> findProductsByCategory(@Param("id_category") int id_category, Pageable pageable);

    @Query(value = "SELECT * FROM product WHERE lower(fn_remove_accents(name)) LIKE concat('%', lower(fn_remove_accents(:name)), '%')", nativeQuery = true)
    Page<Product> findByNameContaining(String name, Pageable pageable);
    @Query(value = "select p from Product p inner join ProductCategory pc on p.id = pc.productCategoryId.id_product where pc.productCategoryId.id_category = :id_category")
    List<Product> findProductsByCategory(@Param("id_category") int id_category);
}
