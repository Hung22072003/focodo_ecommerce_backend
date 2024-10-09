package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/getAllCategories")
    public ApiResponse<List<CategoryDTO>> getAllCategory(
    ) {
        return ApiResponse.<List<CategoryDTO>>builder().result(categoryService.getAllCategories()).build();
    }

    @GetMapping("")
    public ApiResponse<PaginationObjectResponse> getAllCategory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(categoryService.getAllCategories(page, size)).build();
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

    @PostMapping("/addProductToCategory/{id_category}")
    public void addProductToCategory(
            @PathVariable("id_category") int id_category,
            @RequestParam("id_product") int id_product
    ) {
        categoryService.addProductToCategory(id_category, id_product);
    }

    @DeleteMapping("/removeProductFromCategory/{id_category}")
    public void removeProductFromCategory(
            @PathVariable("id_category") int id_category,
            @RequestParam("id_product") int id_product
    ) {
        categoryService.removeProductFromCategory(id_category, id_product);
    }
}
