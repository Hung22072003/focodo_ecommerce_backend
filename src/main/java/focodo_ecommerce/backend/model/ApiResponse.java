package focodo_ecommerce.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>{
    private int code = 1000;
    private String message;
    private T result;
}
