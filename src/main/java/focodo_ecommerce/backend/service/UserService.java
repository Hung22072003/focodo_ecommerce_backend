package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.model.UserProfileRequest;

import java.util.List;

public interface UserService {
    PaginationObjectResponse getAllUsers(int page, int size);

    UserDTO getUser(String name);

    UserDTO getUser(int id);

    UserDTO updateProfileUser(UserProfileRequest userProfileRequest);

    boolean checkPassword(String password);

    void updatePassword(String old_password, String password);

    List<UserDTO> getAllUsersNotPaginated();
}
