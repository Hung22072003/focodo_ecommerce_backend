package focodo_ecommerce.backend.service;

import com.cloudinary.utils.ObjectUtils;
import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.ProductCategory;
import focodo_ecommerce.backend.entity.ProductImage;
import focodo_ecommerce.backend.entity.embeddedID.ProductCategoryId;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.ProductRequest;
import focodo_ecommerce.backend.repository.CategoryRepository;
import focodo_ecommerce.backend.repository.ProductCategoryRepository;
import focodo_ecommerce.backend.repository.ProductImageRepository;
import focodo_ecommerce.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductImageRepository productImageRepository;
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDTO createProduct(ProductRequest productRequest, List<MultipartFile> images) {
        if(productRepository.existsByName(productRequest.getName())) throw new AppException(ErrorCode.PRODUCT_EXIST);
        Product product = new Product(productRequest.getName(), productRequest.getOriginal_price(), productRequest.getSell_price(), productRequest.getDiscount(), productRequest.getSub_description(), productRequest.getMain_description(), productRequest.getQuantity(), productRequest.getPackage_quantity());
        Product saveProduct = productRepository.save(product);
        if(productRequest.getCategories() != null) {
            productRequest.getCategories().forEach((category) -> {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductCategoryId(new ProductCategoryId(saveProduct.getId(),category));
                productCategory.setProduct(saveProduct);
                productCategory.setCategory(categoryRepository.findById(category).orElse(null));
                productCategoryRepository.save(productCategory);
            });
        }

        List<String> productImages = new ArrayList<String>();
        if(images != null) {
            List<CompletableFuture<String>> futures = images.stream()
                    .map(image -> CompletableFuture.supplyAsync(() -> {
                        try {
                            Map result = cloudinary.getInstance().uploader().upload(image.getBytes(), ObjectUtils.asMap("folder", "focodo_ecommerce/product",
                                    "resource_type", "image"));
                            productImageRepository.save(new ProductImage(result.get("secure_url").toString(), saveProduct));
                            return result.get("secure_url").toString();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            productImages.addAll(futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        }
        ProductDTO productDTO = new ProductDTO(saveProduct);
        productDTO.setImages(productImages);
        return productDTO;
    }

    @Override
    public ProductDTO getProductById(int id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductDTO(foundProduct);
    }

    @Override
    public List<ProductDTO> getAllProduct(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("id"))).get().map(ProductDTO::new).toList();
    }

    @Override
    public List<ProductDTO> getProductsByCategory(int page, int size, int id) {
        Page<Product> products = productRepository.findProductsByCategory(id, PageRequest.of(page, size, Sort.by("id").descending()));
        return products.get().map(ProductDTO::new).toList();
    }

    @Override
    public List<ProductDTO> searchProducts(String query, int page, int size) {
        if(query.isEmpty()) return List.of();
        return productRepository.findByNameContaining(query, PageRequest.of(page, size, Sort.by("id").descending())).stream().map(ProductDTO::new).toList();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteProduct(int id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        List<ProductImage> productImages = foundProduct.getProductImageList();
        deleteProductImagesOnCloud(productImages);
        productRepository.delete(foundProduct);
    }

    @Override
    public void deleteProductImagesOnCloud(List<ProductImage> productImages) {
        List<String> nameImages = getNameImages(productImages);
        List<CompletableFuture<Void>> deletionFutures = nameImages.stream()
                .map(name -> CompletableFuture.runAsync(() -> {
                    try {
                        cloudinary.getInstance().api().deleteResources(Arrays.asList(name),
                                ObjectUtils.asMap("type", "upload", "resource_type", "image"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .collect(Collectors.toList());

        CompletableFuture<Void> allDeletions = CompletableFuture.allOf(
                deletionFutures.toArray(new CompletableFuture[0]));

        try {
            allDeletions.get(); // Wait for all deletions to complete
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getNameImages(List<ProductImage> productImages) {
        List<String> nameImages = new ArrayList<String>();
        for(ProductImage productImage: productImages) {
            nameImages.add(getNameOneImage(productImage));
        }
        return nameImages;
    }

    @Override
    public String getNameOneImage(ProductImage productImage) {
        int lastSlashIndex = productImage.getImage().lastIndexOf("/");

        // Cắt chuỗi từ vị trí sau dấu "/" cho đến hết chuỗi
        String fileNameWithExtension = productImage.getImage().substring(lastSlashIndex + 1);

        // Tách phần tên file và phần mở rộng
        String[] parts = fileNameWithExtension.split("\\.");
        return "focodo_ecommerce/product/" + parts[0];
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDTO updateProduct(int id, ProductRequest productRequest, List<MultipartFile> images) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.setName(productRequest.getName());
        foundProduct.setOriginal_price(productRequest.getOriginal_price());
        foundProduct.setSell_price(productRequest.getSell_price());
        foundProduct.setDiscount(productRequest.getDiscount());
        foundProduct.setQuantity(productRequest.getQuantity());
        foundProduct.setPackage_quantity(productRequest.getPackage_quantity());
        productRepository.save(foundProduct);
        return new ProductDTO(foundProduct);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDTO updateDescriptionProduct(int id, String description) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.setMain_description(description);
        productRepository.save(foundProduct);
        return new ProductDTO(foundProduct);
    }
}
