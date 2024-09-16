package focodo_ecommerce.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import focodo_ecommerce.backend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private int id;
    private String name;
    private String image;
    private List<CategoryDTO> subcategories;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.image = category.getImage();
        this.subcategories = category.getSubcategories().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
}
