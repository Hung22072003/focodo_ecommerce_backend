package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.model.UserProfileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    PaginationObjectResponse getAllUsers(int page, int size);

    UserDTO getUser(String name);

    UserDTO getUser(int id);

    UserDTO updateProfileUser(UserProfileRequest userProfileRequest);

    boolean checkPassword(String password);

    void updatePassword(String old_password, String password);

    List<UserDTO> getAllUsersNotPaginated();

    void updateAvatar(MultipartFile avatar);

    UserDTO updateProfileUser(String fullName, String email, String phone, String address, String province, String district, String ward);

    PaginationObjectResponse searchCustomers(String query, int page, int size);
}
