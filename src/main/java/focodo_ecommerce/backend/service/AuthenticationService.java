package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.auth.AuthenticationRequest;
import focodo_ecommerce.backend.auth.AuthenticationResponse;
import focodo_ecommerce.backend.auth.RegisterRequest;
import focodo_ecommerce.backend.config.JwtService;
import focodo_ecommerce.backend.entity.Role;
import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public interface AuthenticationService {
    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public AuthenticationResponse refreshToken(String token);

    String verifyEmail(String email);

    String verifyOtp(String email, String otp);

    Integer generateOtp();
}
