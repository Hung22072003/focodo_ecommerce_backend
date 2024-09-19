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

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXIST);
        }
        var user = User.builder()
                .full_name(request.getFull_name())
                .phone(request.getPhone())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().access_token(jwtToken).refresh_token(jwtRefreshToken).build();
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().access_token(jwtToken).refresh_token(jwtRefreshToken).build();
    }
    @Override
    public AuthenticationResponse refreshToken(String token){
        String email = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(email);
        if(jwtService.isTokenValid(token, user)) {
            var jwtToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponse.builder().access_token(jwtToken).refresh_token(jwtRefreshToken).build();
        } else {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }
}
