package focodo_ecommerce.backend.service;

import com.cloudinary.utils.ObjectUtils;
import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.entity.ImageReview;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final String folderName = "focodo_ecommerce/product";
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;
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

        ProductDTO productDTO = new ProductDTO(saveProduct);
        if(images != null ) {
            List<String> productImages = cloudinaryService.uploadMultipleFiles(images, folderName);
            List<ProductImage> productSavedImages = productImages.stream().map((image) -> new ProductImage(image, saveProduct)).toList();
            productImageRepository.saveAll(productSavedImages);
            productDTO.setImages(productImages);
        }
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
        List<String> deleteImages = productImages.stream().map(ProductImage::getImage).toList();
        cloudinaryService.deleteMultipleFiles(deleteImages, folderName);
        productRepository.delete(foundProduct);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public ProductDTO updateDescriptionProduct(int id, String subDescription, String mainDescription) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if(subDescription != null) foundProduct.setSub_description(subDescription);
        if(mainDescription != null) foundProduct.setMain_description(mainDescription);
        return new ProductDTO(foundProduct);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDTO updateProduct(int id, ProductRequest productRequest, List<MultipartFile> files, List<String> images) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.setName(productRequest.getName());
        foundProduct.setOriginal_price(productRequest.getOriginal_price());
        foundProduct.setSell_price(productRequest.getSell_price());
        foundProduct.setDiscount(productRequest.getDiscount());
        foundProduct.setQuantity(productRequest.getQuantity());
        foundProduct.setPackage_quantity(productRequest.getPackage_quantity());
        List<ProductImage> newImages = new ArrayList<ProductImage>();
        if(files != null ) {
            newImages.addAll(cloudinaryService.uploadMultipleFiles(files, folderName).stream().map((image) -> new ProductImage(image, foundProduct)).toList());
        }

        List<ProductImage> productImages =foundProduct.getProductImageList();
        if(images != null) {
            List<String> deleteImages = new ArrayList<>();
            List<ProductImage> deleteDbImages = new ArrayList<>();
            productImages.forEach((image) -> {
                if(images.contains(image.getImage())) newImages.add(image);
                else {
                    deleteDbImages.add(image);
                    deleteImages.add(image.getImage());
                }
            });
            cloudinaryService.deleteMultipleFiles(deleteImages, folderName);
            productImageRepository.deleteAllInBatch(deleteDbImages);
        } else {
            cloudinaryService.deleteMultipleFiles(productImages.stream().map(ProductImage::getImage).toList(), folderName);
            productImageRepository.deleteAllInBatch(productImages);
            foundProduct.setProductImageList(null);
        }

        foundProduct.setProductImageList(newImages);
        productRepository.save(foundProduct);
        return new ProductDTO(foundProduct);
    }
}
