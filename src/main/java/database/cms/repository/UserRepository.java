package database.cms.repository;

import database.cms.DTO.request.UserRegisterRequest;
import database.cms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
