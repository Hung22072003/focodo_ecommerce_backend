package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.entity.ProductImage;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.model.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductRequest productRequest, List<MultipartFile> images);

    ProductDTO getProductById(int id);

    PaginationObjectResponse getAllProduct(int page, int size);

    PaginationObjectResponse getProductsByCategory(int page, int size, int id);

    PaginationObjectResponse searchProducts(String query, int page, int size);

    void deleteProduct(int id);
    ProductDTO updateDescriptionProduct(int id, String subDescription, String mainDescription);

    ProductDTO updateProduct(int id, ProductRequest productRequest, List<MultipartFile> files, List<String> images);

    List<ProductDTO> getAllProductNotPaginated();

    List<ProductDTO> getProductsByCategory(int id);
}
