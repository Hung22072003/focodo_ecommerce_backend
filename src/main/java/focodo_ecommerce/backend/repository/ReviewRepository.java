package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, JpaSpecificationExecutor<Review> {
    @Query("select r from Review r where r.product.id = :id_product")
    Page<Review> findReviewsByProduct(@Param("id_product") int id_product, Pageable pageable);

    @Query("select r from Review r where r.product.id = :id_product and r.rating = :rating")
    Page<Review> findReviewsByProductAndRating(@Param("id_product") int id_product, @Param("rating") int rating, Pageable pageable);
}
