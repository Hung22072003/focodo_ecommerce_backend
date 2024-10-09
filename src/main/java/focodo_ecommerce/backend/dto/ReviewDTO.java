package focodo_ecommerce.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import focodo_ecommerce.backend.entity.ImageReview;
import focodo_ecommerce.backend.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
    private int id;
    private int rating;
    private String content;
    private LocalDateTime date;
    private ProductDTO product;
    private UserDTO user;
    private List<String> images;
    public ReviewDTO(Review saveReview) {
        this.id = saveReview.getId();
        this.product = new ProductDTO(saveReview.getProduct());
        this.rating = saveReview.getRating();
        this.content = saveReview.getContent();
        this.date = saveReview.getDate();
        this.user = new UserDTO(saveReview.getUser());
        this.images = (saveReview.getImageReviews() != null) ? saveReview.getImageReviews().stream().map(ImageReview::getImage).toList() : null;
    }
}
