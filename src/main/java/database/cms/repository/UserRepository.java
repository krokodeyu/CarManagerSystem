package database.cms.repository;

import database.cms.DTO.request.UserRegisterRequest;
import database.cms.entity.Role;
import database.cms.entity.Role;
import database.cms.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.role = 'ADMIN' ")
    User findAdmin();

    Collection<Object> findByRole(Role role);
}
