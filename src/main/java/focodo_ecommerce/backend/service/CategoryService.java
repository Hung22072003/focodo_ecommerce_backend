package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.model.CategoryRequest;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    PaginationObjectResponse getAllCategories(int page, int size);

    CategoryDTO getCategoryById(int id);

    List<CategoryDTO> getAllCategoriesNotPaginated();

    void addProductToCategory(int idCategory, int idProduct);

    void removeProductFromCategory(int idCategory, int idProduct);

    CategoryDTO createCategory(CategoryRequest category, MultipartFile image);

    void deleteCategory(int id);

    CategoryDTO updateCategory(int id, CategoryRequest category, MultipartFile image);
}
