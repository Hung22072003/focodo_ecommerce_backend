package focodo_ecommerce.backend.auth;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String full_name;
    private String email;
    private String phone;
    private String username;
    @Length(min = 6, message = "PASSWORD_INVALID")
    private String password;
}
