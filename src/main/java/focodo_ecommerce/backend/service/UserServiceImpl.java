package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.UserProfileRequest;
import focodo_ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDTO> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(UserDTO::new).stream().toList();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDTO> getAllUsersNotPaginated() {
        return userRepository.findAll().stream().map(UserDTO::new).toList();
    }
    @Override
    public UserDTO getUser(String name) {
        return new UserDTO(userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public UserDTO getUser(int id) {
        return new UserDTO(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public UserDTO updateProfileUser(UserProfileRequest userProfileRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User foundUser = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        foundUser.setFull_name(userProfileRequest.getFull_name());
        foundUser.setEmail(userProfileRequest.getEmail());
        foundUser.setPhone(userProfileRequest.getPhone());
        foundUser.setAddress(userProfileRequest.getAddress());
        foundUser.setDistrict(userProfileRequest.getDistrict());
        foundUser.setProvince(userProfileRequest.getProvince());
        foundUser.setWard(userProfileRequest.getWard());
        return new UserDTO(foundUser);
    }

    @Override
    public boolean checkPassword(String password) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User foundUser = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return BCrypt.checkpw(password, foundUser.getPassword());
    }

    @Override
    @Transactional
    public void updatePassword(String old_password, String new_password) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User foundUser = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!BCrypt.checkpw(old_password, foundUser.getPassword())) throw new AppException(ErrorCode.OLD_PASSWORD_NOT_CORRECT);
        foundUser.setPassword(passwordEncoder.encode(new_password));
    }

}