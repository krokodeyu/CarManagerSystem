package database.cms.service;

import database.cms.DTO.request.UserRegisterRequest;
import database.cms.DTO.response.RegisterResponse;
import database.cms.entity.User;
import database.cms.exception.AuthErrorException;
import database.cms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        LocalDateTime now = LocalDateTime.now();
        user.setName(name);
        user.setEmail(email);
        user.setCreatedAt(now);
        userRepository.save(user);

        return new RegisterResponse(user.getId(), name, email, now);
    }
}
