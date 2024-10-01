package focodo_ecommerce.backend.model;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.dto.ReviewDTO;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationObjectResponse {
    Object data;
    Pagination pagination;
}
