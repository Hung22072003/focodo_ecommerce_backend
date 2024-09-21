package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.ReviewDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.ReviewRequest;
import focodo_ecommerce.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/all")
    public ApiResponse<List<ReviewDTO>> getAllReviews(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<List<ReviewDTO>>builder().result(reviewService.getAllReviews(page, size)).build();
    }

    @GetMapping("/getReviewsOfProduct/{id}")
    public ApiResponse<List<ReviewDTO>> getReviewsOfProduct(
            @PathVariable("id") int id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "rating", defaultValue = "0") int rating
    ) {
        return ApiResponse.<List<ReviewDTO>>builder().result(reviewService.getReviewsOfProduct(id, page, size, rating)).build();
    }
    @PostMapping("/create")
    public ApiResponse<ReviewDTO> createReview(
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestPart(name = "review", required = false)ReviewRequest reviewRequest
            ) {
        return ApiResponse.<ReviewDTO>builder().result(reviewService.createReview(reviewRequest, images)).build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<ReviewDTO> updateReview(
            @PathVariable("id") int id,
            @RequestParam(name = "files", required = false) List<MultipartFile> files,
            @RequestParam(name = "images", required = false) List<String> images,
            @RequestPart(name = "review", required = false)ReviewRequest reviewRequest
    ) {
        return ApiResponse.<ReviewDTO>builder().result(reviewService.updateReview(id, files, images, reviewRequest)).build();
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<String> deleteReview(
            @PathVariable("id") int id,
            Principal principal
    ) {
        reviewService.deleteReview(id);
        return ApiResponse.<String>builder().result("Delete review successfully").build();
    }
}