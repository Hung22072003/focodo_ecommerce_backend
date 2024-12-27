package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.auth.AuthenticationRequest;
import focodo_ecommerce.backend.auth.AuthenticationResponse;
import focodo_ecommerce.backend.auth.RegisterRequest;
import focodo_ecommerce.backend.config.JwtService;
import focodo_ecommerce.backend.entity.Role;
import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.entity.VerificationCode;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.EmailDetails;
import focodo_ecommerce.backend.repository.UserRepository;
import focodo_ecommerce.backend.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXIST);
        }
        User user = userRepository.findByPhone(request.getPhone()).orElse(null);
        if(user != null) {
            if(user.getRole().equals(Role.USER)) throw new AppException(ErrorCode.PHONE_EXIST);
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setFull_name(request.getFull_name());
            user.setRole(Role.USER);
            user.setCreated_date(LocalDateTime.now());
            userRepository.save(user);
        } else {
            user = User.builder()
                    .full_name(request.getFull_name())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .created_date(LocalDateTime.now())
                    .total_money(0L)
                    .total_order(0)
                    .build();
            userRepository.save(user);
        }
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
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if(jwtService.isTokenValid(token, user)) {
            var jwtToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponse.builder().access_token(jwtToken).refresh_token(jwtRefreshToken).build();
        } else {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    @Override
    public String verifyEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        verificationCodeRepository.findByUser(user).ifPresent(verificationCodeRepository::delete);

        int otp = generateOtp();
        VerificationCode newVerificationCode = VerificationCode.builder()
                .otp(otp)
                .expiration_time(new Date(System.currentTimeMillis() + 60*1000))
                .user(user)
                .build();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(email)
                .subject("Forgot Password - OTP Verification Code")
                .msgBody("Dear " + user.getFull_name() +",\n" +
                        "Thank you for using service with us. To verify your email, please enter the following\n" +
                        "One Time Password (OTP): " + otp + "\n" +
                        "This OTP is valid for 1 minutes from the receipt of this email.\n" +
                        "Best regards,\n" +
                        "Đặc sản Huế - FOCODO")
                .build();

        verificationCodeRepository.save(newVerificationCode);
        emailService.sendSimpleMail(emailDetails);
        return "Verification code is sent to you. Please check your email!";
    }

    @Override
    public String verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        VerificationCode verificationCode = verificationCodeRepository.findByOtpAndUser(otp, user).orElseThrow(() -> new AppException(ErrorCode.OTP_INVALID));
        if(verificationCode.getExpiration_time().before(Date.from(Instant.now()))) {
            verificationCodeRepository.delete(verificationCode);
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
        verificationCodeRepository.delete(verificationCode);
        return "OTP is verified successfully";
    }

    @Override
    public Integer generateOtp() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }

    @Override
    public String checkRoleToken(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if(jwtService.isTokenValid(token, user)) {
            return user.getAuthorities().toString();
        } else {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    @Override
    public Boolean checkAdminRoleToken(String token) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if(jwtService.isTokenValid(token, user)) {
            return !user.getAuthorities().stream().filter((grantedAuthority) -> grantedAuthority.getAuthority().equals("ADMIN")).toList().isEmpty();
        } else {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    @Override
    public Boolean checkTokenExpired(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return jwtService.isTokenValid(token, user);
    }

    @Override
    public String resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "Reset password successfully!";
    }
}
