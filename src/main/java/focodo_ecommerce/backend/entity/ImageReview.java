package focodo_ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "id_review")
    private Review review;
}
