package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.dto.ProductDetailDTO;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.ProductCategory;
import focodo_ecommerce.backend.entity.ProductImage;
import focodo_ecommerce.backend.entity.embeddedID.ProductCategoryId;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.Pagination;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public ProductDetailDTO getProductById(int id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductDetailDTO(foundProduct);
    }

    @Override
    public PaginationObjectResponse getAllProduct(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
        return PaginationObjectResponse.builder().data(products.get().map(ProductDTO::new).toList()).pagination(new Pagination(products.getTotalElements(), products.getTotalPages(), products.getNumber())).build();
    }
    @Override
    public List<ProductDTO> getAllProductNotPaginated() {
        return productRepository.findAll(Sort.by("id").descending()).stream().map(ProductDTO::new).toList();
    }

    @Override
    public List<ProductDTO> getProductsByCategory(int id) {
        return productRepository.findProductsByCategory(id).stream().map(ProductDTO::new).toList();
    }

    @Override
    public List<ProductDTO> getProductsBestSeller() {
        Page<Product> products = productRepository.findProductsBestSeller(PageRequest.of(0, 8, Sort.by("sold_quantity").descending()));
        return products.get().map(ProductDTO::new).toList();
    }

    @Override
    public List<ProductDTO> getProductsDiscount() {
        Page<Product> products = productRepository.findProductsDiscount(PageRequest.of(0, 8, Sort.by("discount").descending()));
        return products.get().map(ProductDTO::new).toList();
    }

    @Override
    public PaginationObjectResponse getProductsByCategory(int page, int size, int id) {
        Page<Product> products = productRepository.findProductsByCategory(id, PageRequest.of(page, size, Sort.by("id").descending()));
        return PaginationObjectResponse.builder().data(products.get().map(ProductDTO::new).toList()).pagination(new Pagination(products.getTotalElements(), products.getTotalPages(), products.getNumber())).build();
    }

    @Override
    public PaginationObjectResponse searchProducts(String query, int page, int size) {
        if(query.isEmpty()) return PaginationObjectResponse.builder().build();
        Page<Product> products = productRepository.findByNameContaining(query, PageRequest.of(page, size, Sort.by("id").descending()));
        return PaginationObjectResponse.builder().data(products.get().map(ProductDTO::new).toList()).pagination(new Pagination(products.getTotalElements(), products.getTotalPages(), products.getNumber())).build();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public void deleteProduct(int id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.set_delete(true);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public void activeProduct(int id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.set_delete(false);
    }

    @Override
    public List<ProductDTO> getRelatedProducts(int id) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        List<ProductDTO> relatedProducts = new ArrayList<>();
        List<Integer> exclusionIds = new ArrayList<>(Arrays.asList(1,2,3));
        foundProduct.getProductCategories().forEach((productCategory -> {
            if (!exclusionIds.contains(productCategory.getCategory().getId())) {
                relatedProducts.addAll(getProductsByCategory(productCategory.getCategory().getId()));
            }
        }));
        return relatedProducts.stream().distinct().toList();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public ProductDetailDTO updateDescriptionProduct(int id, String subDescription, String mainDescription) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if(subDescription != null) foundProduct.setSub_description(subDescription);
        if(mainDescription != null) foundProduct.setMain_description(mainDescription);
        return new ProductDetailDTO(foundProduct);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDetailDTO createProduct(ProductRequest productRequest, List<MultipartFile> images) {
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

        ProductDetailDTO productDTO = new ProductDetailDTO(saveProduct);
        if(images != null ) {
            List<String> productImages = cloudinaryService.uploadMultipleFiles(images, folderName);
            List<ProductImage> productSavedImages = productImages.stream().map((image) -> new ProductImage(image, saveProduct)).toList();
            productImageRepository.saveAll(productSavedImages);
            productDTO.setImages(productImages);
        }
        return productDTO;
    }

    private String calculateFileHash(InputStream fileStream) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5"); // Có thể thay bằng "SHA-1" hoặc "SHA-256" nếu cần

        byte[] dataBytes = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileStream.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, bytesRead);
        }
        byte[] mdBytes = md.digest();

        // Convert byte array thành chuỗi hex
        StringBuilder sb = new StringBuilder();
        for (byte b : mdBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public ProductDetailDTO updateProduct(int id, ProductRequest productRequest, List<MultipartFile> files, List<String> images) {
        Product foundProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        foundProduct.setName(productRequest.getName());
        foundProduct.setOriginal_price(productRequest.getOriginal_price());
        foundProduct.setSell_price(productRequest.getSell_price());
        foundProduct.setDiscount(productRequest.getDiscount());
        foundProduct.setQuantity(productRequest.getQuantity());
        foundProduct.setPackage_quantity(productRequest.getPackage_quantity());
        foundProduct.setSub_description(productRequest.getSub_description());
        foundProduct.setMain_description(productRequest.getMain_description());
        List<ProductImage> newImages = new ArrayList<ProductImage>();

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

        if(files != null ) {
            if(newImages.isEmpty()) {
                newImages.addAll(cloudinaryService.uploadMultipleFiles(files, folderName).stream().map((image) -> new ProductImage(image, foundProduct)).toList());
            } else {
                List<MultipartFile> existFiles = new ArrayList<>();
                for (MultipartFile file : files) {
                    if(checkExistFile(file, newImages)) existFiles.add(file);
                }
                files.removeAll(existFiles);
                newImages.addAll(cloudinaryService.uploadMultipleFiles(files, folderName).stream().map((image) -> new ProductImage(image, foundProduct)).toList());
            }
        }

        foundProduct.setProductImageList(newImages);
        productRepository.save(foundProduct);
        return new ProductDetailDTO(foundProduct);
    }
    private Boolean checkExistFile(MultipartFile file, List<ProductImage> images) {
        for (ProductImage image : images) {
            try {
                String newFileHash = calculateFileHash(file.getInputStream());
                InputStream existingImageStream = new URL(image.getImage()).openStream();
                String existingFileHash = calculateFileHash(existingImageStream);

                if (newFileHash.equals(existingFileHash)) return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
