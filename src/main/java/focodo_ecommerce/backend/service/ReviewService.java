package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ReviewDTO;
import focodo_ecommerce.backend.model.ReviewRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(ReviewRequest reviewRequest, List<MultipartFile> images);

    List<ReviewDTO> getAllReviews(int page, int size);

    List<ReviewDTO> getReviewsOfProduct(int id, int page, int size, int rating);

    void deleteReview(int id);

    ReviewDTO updateReview(int id, List<MultipartFile> files, List<String> images, ReviewRequest reviewRequest);

    List<ReviewDTO> getAllReviewsNotPaginated();

    List<ReviewDTO> getReviewsOfProduct(int id);
}
