package focodo_ecommerce.backend.controller;

import com.cloudinary.Api;
import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.model.UserProfileRequest;
import focodo_ecommerce.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;
    @GetMapping("")
    public ApiResponse<PaginationObjectResponse> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "10") int size) {
        return ApiResponse.<PaginationObjectResponse>builder().result(userService.getAllUsers(page, size)).build();
    }

    @GetMapping("/all")
    public ApiResponse<List<UserDTO>> getAllUsersNotPaginated() {
        return ApiResponse.<List<UserDTO>>builder().result(userService.getAllUsersNotPaginated()).build();
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

    @PutMapping("/updateDetailProfileUser")
    public ApiResponse<UserDTO> updateDetailProfileUser(
            @RequestParam(name = "full_name", required = false) String full_name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "province", required = false) String province,
            @RequestParam(name = "district", required = false) String district,
            @RequestParam(name = "ward", required = false) String ward
    ) {
        return ApiResponse.<UserDTO>builder().result(userService.updateProfileUser(full_name, email, phone, address, province, district, ward)).build();
    }

    @PutMapping("/updateAvatar")
    public void updateAvatar(@RequestParam("avatar") MultipartFile avatar){
        userService.updateAvatar(avatar);
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

    @GetMapping("/search")
    public ApiResponse<PaginationObjectResponse> searchCustomers(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(userService.searchCustomers(query, page, size)).build();
    }
}
