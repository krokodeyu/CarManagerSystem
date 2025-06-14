package database.cms.config;

import database.cms.entity.Role;
import database.cms.entity.User;
import database.cms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            User admin = new User();
            admin.setName("sysadmin");
            admin.setEncryptedPassword(passwordEncoder.encode("123456")); // 初始密码
            admin.setEmail("admin@yourdomain.com");
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("初始管理员账号已创建，用户名: sysadmin");
            System.out.println("请立即登录并修改密码！");
        }
    }
}