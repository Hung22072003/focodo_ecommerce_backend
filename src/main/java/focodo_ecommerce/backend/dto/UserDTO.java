package focodo_ecommerce.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import focodo_ecommerce.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private int id;
    private String full_name;
    private String email;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String role;
    private String username;
    private String avatar;

    public UserDTO(User user) {
        this.id = user.getId();
        this.full_name = user.getFull_name();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.province = user.getProvince();
        this.district = user.getDistrict();
        this.ward = user.getWard();
        this.username = user.getUsername();
        this.role = user.getRole().toString();
        this.avatar = user.getAvatar();
    }
}
