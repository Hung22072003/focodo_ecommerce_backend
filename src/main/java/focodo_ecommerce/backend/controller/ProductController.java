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
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.getProductById(id));
        return apiResponse;
    }

    @GetMapping("/getAllProduct")
    public ApiResponse<List<ProductDTO>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "10") int size
    ) {
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.getAllProduct(page, size));
        return apiResponse;
    }

    @GetMapping("/getProductsByCategory/{id}")
    public ApiResponse<List<ProductDTO>> getProductsByCategory(
            @PathVariable("id") int id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "8") int size
    ) {
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.getProductsByCategory(page, size, id));
        return apiResponse;
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductDTO>> searchProducts(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "4") int size
    ) {
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.searchProducts(query, page, size));
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<ProductDTO> createProduct(
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestPart(name = "product") @Valid ProductRequest productRequest
    ) {
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.createProduct(productRequest, images));
        return apiResponse;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ProductDTO> updateProduct(
            @PathVariable("id") int id,
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestPart(name = "product") @Valid ProductRequest productRequest
    ) {
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.updateProduct(id, productRequest, images));
        return apiResponse;
    }

    @PutMapping("/updateDescription/{id}")
    public ApiResponse<ProductDTO> updateDescription(
            @PathVariable("id") int id,
            @RequestParam(name = "description", required = false) String description
    ) {
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.updateDescriptionProduct(id, description));
        return apiResponse;
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteProduct(
            @PathVariable("id") int id
    ) {
        productService.deleteProduct(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult("Delete product successfully");
        return apiResponse;
    }
}
