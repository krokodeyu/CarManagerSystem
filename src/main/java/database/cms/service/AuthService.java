package database.cms.service;

import database.cms.DTO.request.LoginRequest;
import database.cms.DTO.response.JWTResponse;
import database.cms.entity.LoginInfo;
import database.cms.entity.Technician;
import database.cms.entity.User;
import database.cms.exception.AuthErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.TechnicianRepository;
import database.cms.repository.UserRepository;
import database.cms.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TechnicianRepository technicianRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TechnicianRepository technicianRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.technicianRepository = technicianRepository;
    }

    @Transactional(readOnly = true)
    public JWTResponse Login(LoginRequest request){
        String name = request.username();
        User user = userRepository.findByName(name);
        if(user != null && passwordEncoder
                .matches(request.password(), user.getEncryptedPassword())) {
            LoginInfo loginInfo = new LoginInfo(user.getId(), user.getName(), user.getRole());
            String token = JWTUtil.generateToken(loginInfo);
            return new JWTResponse(token);
        }

        Technician tech = technicianRepository.findByName(name);
        if(tech != null && passwordEncoder
                .matches(request.password(), tech.getEncryptedPassword())) {
            LoginInfo loginInfo = new LoginInfo(tech.getId(), tech.getName(), tech.getRole());
            String token = JWTUtil.generateToken(loginInfo);
            return new JWTResponse(token);
        }

        throw new AuthErrorException("INVALID_LOGIN_INFO", "用户名或密码错误");
    }


}
