package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> getAllCategories(int page, int size);

    CategoryDTO getCategoryById(int id);

    List<CategoryDTO> getAllCategoriesNotPaginated();
}
