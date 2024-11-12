package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CategoryDTO;
import focodo_ecommerce.backend.entity.Category;
import focodo_ecommerce.backend.entity.ProductCategory;
import focodo_ecommerce.backend.entity.embeddedID.ProductCategoryId;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.CategoryRequest;
import focodo_ecommerce.backend.model.Pagination;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.repository.CategoryRepository;
import focodo_ecommerce.backend.repository.ProductCategoryRepository;
import focodo_ecommerce.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final String folderName = "focodo_ecommerce/category";
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().filter((category) -> category.getParent_category() == null).map(CategoryDTO::new).toList();
    }

    @Override
    public PaginationObjectResponse getAllCategories(int page, int size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
        return PaginationObjectResponse.builder().data(categories.get().map(CategoryDTO::new).toList()).pagination(new Pagination(categories.getTotalElements(), categories.getTotalPages(), categories.getNumber())).build();
    }

    @Override
    public List<CategoryDTO> getAllCategoriesNotPaginated() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }

    @Override
    public void addProductToCategory(int idCategory, int idProduct) {
        Category foundCategory = categoryRepository.findById(idCategory).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        productCategoryRepository.save(new ProductCategory(new ProductCategoryId(idProduct, idCategory),productRepository.findById(idProduct).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)), foundCategory));
    }

    @Override
    public void removeProductFromCategory(int idCategory, int idProduct) {
        productCategoryRepository.delete(productCategoryRepository.findById(new ProductCategoryId(idProduct, idCategory)).orElseThrow());
    }

    @Override
    public CategoryDTO createCategory(CategoryRequest category, MultipartFile image) {
        if(categoryRepository.existsByName(category.getName())) throw new AppException(ErrorCode.CATEGORY_EXIST);
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());
        newCategory.setParent_category(categoryRepository.findById(category.getParent_category()).orElse(null));
        if(image != null) {
            String categoryImage = cloudinaryService.uploadOneFile(image, folderName);
            newCategory.setImage(categoryImage);
        }
        return new CategoryDTO(categoryRepository.save(newCategory));
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
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
    @Override
    @Transactional
    public CategoryDTO updateCategory(int id, CategoryRequest category, MultipartFile image) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        foundCategory.setName(category.getName());
        foundCategory.setDescription(category.getDescription());
        foundCategory.setParent_category(categoryRepository.findById(category.getParent_category()).orElse(null));
        if(image != null) {
            if(foundCategory.getImage() != null) {
                try {
                    String newFileHash = calculateFileHash(image.getInputStream());
                    InputStream existingImageStream = new URL(foundCategory.getImage()).openStream();
                    String existingFileHash = calculateFileHash(existingImageStream);

                    if (!newFileHash.equals(existingFileHash))
                        foundCategory.setImage(cloudinaryService.uploadOneFile(image, folderName));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            } else {
                foundCategory.setImage(cloudinaryService.uploadOneFile(image, folderName));
            }
        }
        return new CategoryDTO(foundCategory);
    }

     @Override
    public List<CategoryDTO> getCategoriesByOptions(List<String> options) {
        List<CategoryDTO> categoryDTOS = getAllCategoriesNotPaginated();
        return categoryDTOS.stream().filter((categoryDTO) -> options.contains((categoryDTO.getName()))).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(int id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryDTO(foundCategory);
    }
}
