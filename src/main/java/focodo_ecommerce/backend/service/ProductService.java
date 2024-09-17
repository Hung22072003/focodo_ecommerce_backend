package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.entity.ProductImage;
import focodo_ecommerce.backend.model.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductRequest productRequest, List<MultipartFile> images);

    ProductDTO getProductById(int id);

    List<ProductDTO> getAllProduct(int page, int size);

    List<ProductDTO> getProductsByCategory(int page, int size, int id);

    List<ProductDTO> searchProducts(String query, int page, int size);

    void deleteProduct(int id);

    void deleteProductImagesOnCloud(List<ProductImage> productImages);

    List<String> getNameImages(List<ProductImage> productImages);

    String getNameOneImage(ProductImage productImage);

    ProductDTO updateProduct(int id, ProductRequest productRequest, List<MultipartFile> images);

    ProductDTO updateDescriptionProduct(int id, String description);
}
