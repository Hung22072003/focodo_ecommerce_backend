package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.entity.Category;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.Pagination;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public PaginationObjectResponse getAllCategories(int page, int size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
        return PaginationObjectResponse.builder().data(categories.get().map(CategoryDTO::new).toList()).pagination(new Pagination(categories.getTotalElements(), categories.getTotalPages(), categories.getNumber())).build();
    }

    @Override
    public List<CategoryDTO> getAllCategoriesNotPaginated() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }
    @Override
    public CategoryDTO getCategoryById(int id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryDTO(foundCategory);
    }

}
