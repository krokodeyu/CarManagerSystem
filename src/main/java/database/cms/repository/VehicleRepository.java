package database.cms.repository;

import database.cms.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByIdAndUserId(Long vehicleId, Long userId);

    List<Vehicle> findAllByUserId(Long userId);
}
