package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.entity.Category;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().filter((category) -> category.getParent_category() == null).map(CategoryDTO::new).toList();
    }

    @Override
    public List<CategoryDTO> getAllCategories(int page, int size) {
        return categoryRepository.findAll(PageRequest.of(page, size, Sort.by("id"))).get().map(CategoryDTO::new).toList();
    }

    @Override
    public CategoryDTO getCategoryById(int id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryDTO(foundCategory);
    }
}
