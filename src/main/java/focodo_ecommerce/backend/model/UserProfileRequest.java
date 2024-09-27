package focodo_ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    private String full_name;
    private String email;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
}