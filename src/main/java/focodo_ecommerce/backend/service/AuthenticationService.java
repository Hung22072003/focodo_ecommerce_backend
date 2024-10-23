package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.auth.AuthenticationRequest;
import focodo_ecommerce.backend.auth.AuthenticationResponse;
import focodo_ecommerce.backend.auth.RegisterRequest;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public AuthenticationResponse refreshToken(String token);

    String verifyEmail(String email);

    String verifyOtp(String email, String otp);

    Integer generateOtp();

    String checkRoleToken(String token);

    Boolean checkAdminRoleToken(String token);

    Boolean checkTokenExpired(String token);

    String resetPassword(String email, String password);
}
