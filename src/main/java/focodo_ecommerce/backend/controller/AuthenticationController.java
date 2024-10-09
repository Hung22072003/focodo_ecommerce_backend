package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.auth.AuthenticationRequest;
import focodo_ecommerce.backend.auth.AuthenticationResponse;
import focodo_ecommerce.backend.service.AuthenticationService;
import focodo_ecommerce.backend.auth.RegisterRequest;
import focodo_ecommerce.backend.model.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.register(request)).build();
    }


    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.authenticate(request)).build();
    }

    @GetMapping("/checkAdminRoleToken")
    public ApiResponse<Boolean> checkAdminRoleToken(
            @RequestParam("token") String token
    ) {
        return ApiResponse.<Boolean>builder().result(authenticationService.checkAdminRoleToken(token)).build();
    }

    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticationResponse> refreshToken(
            @RequestParam(value = "refreshToken") String token
    ){
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.refreshToken(token)).build();
    }


    @PostMapping("/verifyEmail")
    public ApiResponse<String> verifyEmail(
            @RequestParam(value = "email") String email
    ){
        return ApiResponse.<String>builder().result(authenticationService.verifyEmail(email)).build();
    }

    @PostMapping("/verifyOtp")
    public ApiResponse<String> verifyOtp(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "otp") String otp
    ){
        return ApiResponse.<String>builder().result(authenticationService.verifyOtp(email, otp)).build();
    }
}
