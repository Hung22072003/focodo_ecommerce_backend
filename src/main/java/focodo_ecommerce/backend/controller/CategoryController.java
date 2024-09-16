package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.CategoryDTO;
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
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.getAllCategories());
        return apiResponse;
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryDTO>> getAllCategory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.getAllCategories(page, size));
        return apiResponse;
    }

    @GetMapping("/getCategoryById/{id}")
    public ApiResponse<CategoryDTO> getCategoryById(
            @PathVariable("id") int id
    ) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.getCategoryById(id));
        return apiResponse;
    }
}
