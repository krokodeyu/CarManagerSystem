package database.cms.service;

import database.cms.DTO.request.UserRegisterRequest;
import database.cms.DTO.response.RegisterResponse;
import database.cms.entity.Role;
import database.cms.entity.User;
import database.cms.exception.AuthErrorException;
import database.cms.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

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
}
