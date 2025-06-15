package database.cms.repository;

import database.cms.entity.Appointment;
import database.cms.entity.Vehicle;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByIdAndUserId(Long vehicleId, Long userId);

    List<Vehicle> findAllByUserId(Long userId);

    Optional<List<Vehicle>> findAllByModel(String model);

    @Query("SELECT SUM(a.totalCost), COUNT(a) " +
            "FROM Appointment a " +
            "WHERE a.vehicle.model = :model " +
            "AND a.status NOT IN :excludedStatuses")
    Tuple sumTotalCostAndCount(@Param("model") String model, @Param("excludedStatuses") List<Appointment.Status> excludedStatuses);

    @Query("SELECT a.vehicle.model, COUNT(a) " +
            "FROM Appointment a " +
            "WHERE a.status NOT IN :excludedStatuses " +
            "GROUP BY a.vehicle.model")
    List<Tuple> countValidAppointmentsByModel(@Param("excludedStatuses") List<Appointment.Status> excludedStatuses);


}
