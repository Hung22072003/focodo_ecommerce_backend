package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/getAllCategories")
    public ApiResponse<List<CategoryDTO>> getAllCategory(
    ) {
        return ApiResponse.<List<CategoryDTO>>builder().result(categoryService.getAllCategories()).build();
    }

    @GetMapping("")
    public ApiResponse<List<CategoryDTO>> getAllCategory(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return ApiResponse.<List<CategoryDTO>>builder().result(categoryService.getAllCategories(page, size)).build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryDTO>> getAllCategoryNotPaginated(
    ) {
        return ApiResponse.<List<CategoryDTO>>builder().result(categoryService.getAllCategoriesNotPaginated()).build();
    }

    @GetMapping("/getCategoryById/{id}")
    public ApiResponse<CategoryDTO> getCategoryById(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<CategoryDTO>builder().result(categoryService.getCategoryById(id)).build();
    }
}
