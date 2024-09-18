package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.ProductRequest;
import focodo_ecommerce.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/getProductById/{id}")
    public ApiResponse<ProductDTO> getProductById(
            @PathVariable int id
    ) {
        return ApiResponse.<ProductDTO>builder().result(productService.getProductById(id)).build();
    }

    @GetMapping("/getAllProduct")
    public ApiResponse<List<ProductDTO>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "10") int size
    ) {
        return ApiResponse.<List<ProductDTO>>builder().result(productService.getAllProduct(page, size)).build();
    }

    @GetMapping("/getProductsByCategory/{id}")
    public ApiResponse<List<ProductDTO>> getProductsByCategory(
            @PathVariable("id") int id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "8") int size
    ) {
        return ApiResponse.<List<ProductDTO>>builder().result(productService.getProductsByCategory(page, size, id)).build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductDTO>> searchProducts(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "4") int size
    ) {
        return ApiResponse.<List<ProductDTO>>builder().result(productService.searchProducts(query, page, size)).build();
    }

    @PostMapping("/create")
    public ApiResponse<ProductDTO> createProduct(
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestPart(name = "product") @Valid ProductRequest productRequest
    ) {
        return ApiResponse.<ProductDTO>builder().result(productService.createProduct(productRequest, images)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ProductDTO> updateProduct(
            @PathVariable("id") int id,
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestPart(name = "product") @Valid ProductRequest productRequest
    ) {
        return ApiResponse.<ProductDTO>builder().result(productService.updateProduct(id, productRequest, images)).build();
    }

    @PutMapping("/updateDescription/{id}")
    public ApiResponse<ProductDTO> updateDescription(
            @PathVariable("id") int id,
            @RequestParam(name = "description", required = false) String description
    ) {
        return ApiResponse.<ProductDTO>builder().result(productService.updateDescriptionProduct(id, description)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteProduct(
            @PathVariable("id") int id
    ) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder().result("Delete product successfully").build();
    }
}
