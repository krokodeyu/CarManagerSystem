package database.cms.repository;

import database.cms.entity.Appointment;
import database.cms.entity.Vehicle;
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

    List<Appointment.Status> excludedStatuses = Arrays.asList(Appointment.Status.CANCELLED, Appointment.Status.UNACCEPTED);
    List<Vehicle> findAllByUserId(Long userId);

    Optional<List<Vehicle>> findAllByModel(String model);

    @Query("SELECT SUM(a.totalCost), COUNT(a) " +
            "FROM Appointment a " +
            "WHERE a.vehicle.model = :model " +
            "AND a.status NOT IN :excludedStatuses")
    Object[] sumTotalCostAndCount(@Param("model") String model);

    @Query("SELECT a.vehicle.model, COUNT(a) FROM Appointment a " +
            "WHERE a.status NOT IN :excludedStatuses " +
            "GROUP BY a.vehicle.model")
    List<Object[]> countValidAppointmentsByModel();


}
