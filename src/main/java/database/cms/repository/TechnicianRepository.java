package database.cms.repository;

import database.cms.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Technician findByName(String name);
    boolean existsByPhone(String phone);
}
