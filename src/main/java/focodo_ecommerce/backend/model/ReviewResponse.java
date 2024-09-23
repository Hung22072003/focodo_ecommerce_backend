package focodo_ecommerce.backend.model;

import focodo_ecommerce.backend.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private double avg_rating;
    private int total_review;
    private int one_star_quantity = 0;
    private int two_star_quantity = 0;;
    private int three_star_quantity = 0;
    private int four_star_quantity = 0;
    private int five_star_quantity = 0;

    public ReviewResponse(List<Review> reviews) {
        this.total_review = reviews.size();
        this.avg_rating = (double) reviews.stream().map(Review::getRating).reduce(0, Integer::sum) / total_review;
        reviews.forEach((review) -> {
            switch (review.getRating()) {
                case 1: one_star_quantity++; break;
                case 2: two_star_quantity++; break;
                case 3: three_star_quantity++; break;
                case 4: four_star_quantity++; break;
                case 5: five_star_quantity++; break;
                default:
            }
        });
    }

}
