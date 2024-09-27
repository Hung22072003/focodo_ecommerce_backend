package focodo_ecommerce.backend.controller;

import com.cloudinary.Api;
import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.UserProfileRequest;
import focodo_ecommerce.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/all")
    public ApiResponse<List<UserDTO>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "10") int size) {
        return ApiResponse.<List<UserDTO>>builder().result(userService.getAllUsers(page, size)).build();
    }

    @GetMapping("/getUser")
    public ApiResponse<UserDTO> getUser(Principal principal) {
        return ApiResponse.<UserDTO>builder().result(userService.getUser(principal.getName())).build();
    }

    @GetMapping("/getUser/{id}")
    public ApiResponse<UserDTO> getUser(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<UserDTO>builder().result(userService.getUser(id)).build();
    }

    @PutMapping("/updateProfileUser")
    public ApiResponse<UserDTO> updateProfileUser(
            @RequestBody UserProfileRequest userProfileRequest
            ) {
        return ApiResponse.<UserDTO>builder().result(userService.updateProfileUser(userProfileRequest)).build();
    }

    @PostMapping("/checkPassword")
    public ApiResponse<Boolean> checkPassword(
            @RequestParam(value = "password") String password
    ) {
        return ApiResponse.<Boolean>builder().result(userService.checkPassword(password)).build();
    }

    @PutMapping("/updatePassword")
    public void updatePassword(
            @RequestParam(value = "old_password") String old_password,
            @RequestParam(value = "new_password") String new_password
    ) {
        userService.updatePassword(old_password, new_password);
    }
}
