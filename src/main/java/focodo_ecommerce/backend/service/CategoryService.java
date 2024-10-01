package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    PaginationObjectResponse getAllCategories(int page, int size);

    CategoryDTO getCategoryById(int id);

    List<CategoryDTO> getAllCategoriesNotPaginated();
}
