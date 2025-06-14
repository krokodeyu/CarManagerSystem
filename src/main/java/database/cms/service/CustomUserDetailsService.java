package database.cms.service;

import database.cms.detail.CustomUserDetails;
import database.cms.entity.Technician;
import database.cms.entity.User;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.TechnicianRepository;
import database.cms.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TechnicianRepository technicianRepository;

    public CustomUserDetailsService(UserRepository userRepository, TechnicianRepository technicianRepository) {

        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 先查用户表
        User user = userRepository.findByName(username)
                .orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND", "无效的用户名"));
        if (user != null) {
            return new CustomUserDetails(
                    user.getId(),
                    user.getName(),
                    user.getEncryptedPassword(),
                    Collections.singleton(() -> "ROLE_" + user.getRole().name())
            );
        }

        // 再查维修员表
        Technician tech = technicianRepository.findByName(username)
                .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND", "无效的技工名称"));
        if (tech != null) {
            return new CustomUserDetails(
                    tech.getId(),
                    tech.getName(),
                    tech.getEncryptedPassword(),
                    Collections.singleton(() -> "ROLE_" + tech.getRole().name())
            );
        }

        throw new UsernameNotFoundException("用户名不存在：" + username);
    }
}