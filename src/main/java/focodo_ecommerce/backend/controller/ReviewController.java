package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.ReviewDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.model.ReviewRequest;
import focodo_ecommerce.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@CrossOrigin
public class ReviewController {
    private final ReviewService reviewService;
    private final ObjectMapper objectMapper; // Sử dụng ObjectMapper từ Spring context

    @GetMapping("")
    public ApiResponse<PaginationObjectResponse> getAllReviews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(reviewService.getAllReviews(page, size)).build();
    }

    @GetMapping("/getReviewsOfProduct/{id}")
    public ApiResponse<PaginationObjectResponse> getReviewsOfProduct(
            @PathVariable("id") int id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "rating", defaultValue = "0") int rating
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(reviewService.getReviewsOfProduct(id, page, size, rating)).build();
    }

    @GetMapping("/getReviewsByProduct/{id}")
    public ApiResponse<List<ReviewDTO>> getReviewsOfProduct(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<List<ReviewDTO>>builder().result(reviewService.getReviewsOfProduct(id)).build();
    }

    @GetMapping("/getReviewsOfUser")
    public ApiResponse<PaginationObjectResponse> getReviewsOfUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(reviewService.getReviewsOfUser(page, size)).build();
    }

    @GetMapping("/getReviewsByIdUser/{id}")
    public ApiResponse<PaginationObjectResponse> getReviewsByIdUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @PathVariable("id") int id
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(reviewService.getReviewsByIdUser(id, page, size)).build();
    }

    @GetMapping("/getReviewsOfOrder/{id_order}")
    public ApiResponse<List<ReviewDTO>> getReviewsOfOrder(
            @PathVariable("id_order") String id_order
    ) {
        return ApiResponse.<List<ReviewDTO>>builder().result(reviewService.getReviewsOfOrder(id_order)).build();
    }

    @GetMapping("/getReviewById/{id}")
    public ApiResponse<ReviewDTO> getReviewById(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<ReviewDTO>builder().result(reviewService.getReviewById(id)).build();
    }

    @PostMapping("/create")
    public ApiResponse<ReviewDTO> createReview(
            @RequestParam(name = "id_order") String id_order,
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestParam(name = "review") String reviewJson) {
        try {
            ReviewRequest reviewRequest = objectMapper.readValue(reviewJson, ReviewRequest.class);
            return ApiResponse.<ReviewDTO>builder().result(reviewService.createReview(reviewRequest, images, id_order)).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse review JSON", e);
        }
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ReviewDTO> updateReview(
            @PathVariable("id") int id,
            @RequestParam(name = "files", required = false) List<MultipartFile> files,
            @RequestParam(name = "images", required = false) List<String> images,
            @RequestPart(name = "review", required = false) String reviewJson
    ) {
        try {
            ReviewRequest reviewRequest = objectMapper.readValue(reviewJson, ReviewRequest.class);
            return ApiResponse.<ReviewDTO>builder().result(reviewService.updateReview(id, files, images, reviewRequest)).build();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to parse review JSON", e);
        }
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<String> deleteReview(
            @PathVariable("id") int id
    ) {
        reviewService.deleteReview(id);
        return ApiResponse.<String>builder().result("Delete review successfully").build();
    }

    // Tìm kiếm toàn bộ đánh giá
    @GetMapping("/search")
    public ApiResponse<PaginationObjectResponse> searchReviews(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(reviewService.searchReviews(query, page, size)).build();
    }

    // Tìm kiếm đánh giá của một customer
    @GetMapping("/searchReviewsOfUser")
    public ApiResponse<PaginationObjectResponse> searchReviewsOfUser(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size,
            @RequestParam(name = "id_user") int id_user
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(reviewService.searchReviewsOfUser(query,id_user, page, size)).build();
    }
}
