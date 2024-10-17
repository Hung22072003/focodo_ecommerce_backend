package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ReviewDTO;
import focodo_ecommerce.backend.entity.ImageReview;
import focodo_ecommerce.backend.entity.Review;
import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.Pagination;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.model.ReviewRequest;
import focodo_ecommerce.backend.repository.ImageReviewRepository;
import focodo_ecommerce.backend.repository.ProductRepository;
import focodo_ecommerce.backend.repository.ReviewRepository;
import focodo_ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final String folderName = "focodo_ecommerce/review";
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ImageReviewRepository imageReviewRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    @Override
    public ReviewDTO createReview(ReviewRequest reviewRequest, List<MultipartFile> images) {
        Review review = new Review();
        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());
        review.setDate(LocalDateTime.now());
        review.setProduct(productRepository.findById(reviewRequest.getId_product()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        review.setUser(userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        Review saveReview = reviewRepository.save(review);
        ReviewDTO reviewDTO = new ReviewDTO(saveReview);
        if(images != null) {
            List<String> reviewImages = cloudinaryService.uploadMultipleFiles(images, folderName);
            List<ImageReview> reviewSaveImages = reviewImages.stream().map((image) -> new ImageReview(image, saveReview)).toList();
            imageReviewRepository.saveAll(reviewSaveImages);
            reviewDTO.setImages(reviewImages);
        }
        return reviewDTO;
    }

    @Override
    public PaginationObjectResponse getAllReviews(int page, int size) {
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
        return PaginationObjectResponse.builder().data(reviews.get().map(ReviewDTO::new).toList()).pagination(new Pagination(reviews.getTotalElements(),reviews.getTotalPages(),reviews.getNumber())).build();
    }

    @Override
    public PaginationObjectResponse getReviewsOfProduct(int id, int page, int size, int rating) {
        Page<Review> reviews;
        if(rating == 0) {
            reviews = reviewRepository.findReviewsByProduct(id, PageRequest.of(page, size, Sort.by("date").descending()));
        } else {
            reviews = reviewRepository.findReviewsByProductAndRating(id, rating, PageRequest.of(page, size, Sort.by("date").descending()));
        }
        return PaginationObjectResponse.builder().data(reviews.get().map(ReviewDTO::new).toList()).pagination(new Pagination(reviews.getTotalElements(),reviews.getTotalPages(),reviews.getNumber())).build();
    }

    @Override
    public void deleteReview(int id) {
        Review foundReview = reviewRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName().equals(foundReview.getUser().getUsername()) || !authentication.getAuthorities().stream().filter((grantedAuthority) -> grantedAuthority.getAuthority().equals("ADMIN")).toList().isEmpty()) {
            List<ImageReview> imageReviews = foundReview.getImageReviews();
            List<String> deleteImages = imageReviews.stream().map(ImageReview::getImage).toList();
            cloudinaryService.deleteMultipleFiles(deleteImages, folderName);
            reviewRepository.delete(foundReview);
        } else {
            throw new RuntimeException("You do not have permission to delete review");
        }
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(int id, List<MultipartFile> files, List<String> images, ReviewRequest reviewRequest){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Review foundReview = reviewRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        if(authentication.getName().equals(foundReview.getUser().getUsername())){
            foundReview.setDate(LocalDateTime.now());
            foundReview.setContent(reviewRequest.getContent());
            foundReview.setRating(reviewRequest.getRating());
            List<ImageReview> newImages = new ArrayList<ImageReview>();
            if(files != null ) {
                newImages.addAll(cloudinaryService.uploadMultipleFiles(files, folderName).stream().map((image) -> new ImageReview(image, foundReview)).toList());
            }

            List<ImageReview> imageReviews =foundReview.getImageReviews();
            System.out.println(images);
            if(images != null) {
                List<String> deleteImages = new ArrayList<>();
                List<ImageReview> deleteDbImages = new ArrayList<>();
                imageReviews.forEach((image) -> {
                    if(images.contains(image.getImage())) newImages.add(image);
                    else {
                        deleteDbImages.add(image);
                        deleteImages.add(image.getImage());
                    }
                });
                cloudinaryService.deleteMultipleFiles(deleteImages, folderName);
                imageReviewRepository.deleteAllInBatch(deleteDbImages);
            } else {
                cloudinaryService.deleteMultipleFiles(imageReviews.stream().map(ImageReview::getImage).toList(), folderName);
                imageReviewRepository.deleteAllInBatch(imageReviews);
                foundReview.setImageReviews(null);
            }

            foundReview.setImageReviews(newImages);
            return new ReviewDTO(foundReview);
        } else {
            throw new RuntimeException("You do not have permission to update review");
        }
    }

    @Override
    public List<ReviewDTO> getAllReviewsNotPaginated() {
        return reviewRepository.findAll(Sort.by("id").descending()).stream().map(ReviewDTO::new).toList();
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(int id) {
        return reviewRepository.findReviewsByProduct(id).stream().sorted(Comparator.comparing(Review::getDate).reversed()).map(ReviewDTO::new).toList();
    }

    @Override
    public PaginationObjectResponse getReviewsOfUser(int page, int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Page<Review> reviews = reviewRepository.findReviewsByUser(user, PageRequest.of(page, size, Sort.by("date").descending()));
        return PaginationObjectResponse.builder().data(reviews.get().map(ReviewDTO::new).toList()).pagination(new Pagination(reviews.getTotalElements(), reviews.getTotalPages(), reviews.getNumber())).build();
    }
}
