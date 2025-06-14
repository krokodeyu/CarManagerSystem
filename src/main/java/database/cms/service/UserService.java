package database.cms.service;

import database.cms.DTO.request.UserRegisterRequest;
import database.cms.DTO.request.UserUpdateRequest;
import database.cms.DTO.response.NotificationResponse;
import database.cms.DTO.response.RegisterResponse;
import database.cms.DTO.response.UserInfoResponse;
import database.cms.entity.Role;
import database.cms.entity.User;
import database.cms.exception.AuthErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserInfoResponse generateResponse(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "未找到用户"));
        return generateResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserInfoResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserInfoResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(generateResponse(user));
        }
        return responses;
    }

    @Transactional
    public RegisterResponse register(UserRegisterRequest request){
        if (userRepository.existsByName(request.name())) {
            throw new AuthErrorException("USERNAME_EXISTED", "用户名已存在");
        } else if (userRepository.existsByEmail(request.email())) {
            throw new AuthErrorException("EMAIL_EXISTED", "邮箱已被注册");
        }
        User user = new User();
        String name = request.name();
        String email = request.email();
        String encryptedPassword = passwordEncoder.encode(request.password());
        user.setRole(Role.USER);
        user.setName(name);
        user.setEmail(email);
        user.setEncryptedPassword(encryptedPassword);
        userRepository.save(user);

        return new RegisterResponse(user.getId(), name, email, user.getCreatedAt());
    }

    @Transactional
    public UserInfoResponse update(UserUpdateRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "用户不存在"));
        if(request.name() != null){
            user.setName(request.name());
        }
        if(request.email() != null){
            user.setEmail(request.email());
        }
        if(request.password() != null){
            String encryptedPassword = passwordEncoder.encode(request.password());
            user.setEncryptedPassword(encryptedPassword);
        }

        return generateResponse(user);
    }

    public NotificationResponse checkNotification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "用户不存在"));

        return new NotificationResponse(user.getNotification());
    }
}
