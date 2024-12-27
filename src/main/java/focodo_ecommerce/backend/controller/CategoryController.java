package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.CategoryRequest;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/getCategoriesByOptions")
    public ApiResponse<List<CategoryDTO>> getCategoriesByOptions(
            @RequestParam(name = "options") List<String> options
    ) {
        return ApiResponse.<List<CategoryDTO>>builder().result(categoryService.getCategoriesByOptions(options)).build();
    }
    @GetMapping("/getCategoryById/{id}")
    public ApiResponse<CategoryDTO> getCategoryById(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<CategoryDTO>builder().result(categoryService.getCategoryById(id)).build();
    }

    @PostMapping("/create")
    public ApiResponse<CategoryDTO> createCategory(
             @RequestPart("category")CategoryRequest category,
             @RequestParam(name = "image", required = false)MultipartFile image
    ) {
        return ApiResponse.<CategoryDTO>builder().result(categoryService.createCategory(category, image)).build();
    }


    @PutMapping("/update/{id}")
    public ApiResponse<CategoryDTO> updateCategory(
            @PathVariable("id")int id,
            @RequestPart("category")CategoryRequest category,
            @RequestParam(name = "image", required = false)MultipartFile image
    ) {
        return ApiResponse.<CategoryDTO>builder().result(categoryService.updateCategory(id, category, image)).build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(
            @PathVariable("id")int id
    ) {
        categoryService.deleteCategory(id);
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
    @GetMapping("/search")
    public ApiResponse<PaginationObjectResponse> searchCategories(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(categoryService.searchCategories(query, page, size)).build();
    }
}
