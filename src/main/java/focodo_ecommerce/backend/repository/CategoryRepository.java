package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    @Query(value = "select c from Category c where c.name LIKE concat('%', :query, '%')")
    Page<Category> findByNameContaining(String query, Pageable pageable);
}
