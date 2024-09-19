package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.auth.AuthenticationRequest;
import focodo_ecommerce.backend.auth.AuthenticationResponse;
import focodo_ecommerce.backend.service.AuthenticationService;
import focodo_ecommerce.backend.auth.RegisterRequest;
import focodo_ecommerce.backend.model.ApiResponse;
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
            @RequestBody RegisterRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.register(request)).build();
    }


    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.authenticate(request)).build();
    }

    @PostMapping("/refreshToken")
    public ApiResponse<AuthenticationResponse> refreshToken(
            @RequestParam(value = "refreshToken") String token
    ){
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationService.refreshToken(token)).build();
    }


}