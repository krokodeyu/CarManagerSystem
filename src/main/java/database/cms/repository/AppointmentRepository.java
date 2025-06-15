package database.cms.repository;

import database.cms.entity.Appointment;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll();

    Optional<Appointment> findById(long id);

    Boolean existsByAppointmentId(String appointmentId);

    boolean existsByIdAndUserId(Long orderId, Long userId);
    List<Appointment.Status> resolvedOrderStatus = Arrays.asList(Appointment.Status.CANCELLED, Appointment.Status.FINISHED);

    @Query("SELECT a.id " +
            "FROM Appointment a " +
            "WHERE a.status = :status ")
    List<Object[]> findAppointmentsByStatus(@Param("status") Appointment.Status status);


    @Query("SELECT ri.description as description, COUNT(ri) as count " +
            "FROM Appointment a " +
            "JOIN a.vehicle v " +
            "JOIN a.repairItems ri " +
            "WHERE v.model = :model " +
            "GROUP BY ri.description " +
            "ORDER BY COUNT(ri) DESC")
    List<Tuple> findMostFrequentFailureByModel(
            @Param("model") String model,
            Pageable pageable
    );

    @Query("SELECT ap.sparePart.name, SUM(ap.sparePart.price * ap.quantity) " +
            "FROM Appointment a " +
            "JOIN a.appointmentParts ap " +
            "WHERE FUNCTION('YEAR', a.createdAt) = :year " +
            "AND FUNCTION('MONTH', a.createdAt) = :month " +
            "GROUP BY ap.sparePart.name " +
            "ORDER BY SUM(ap.sparePart.price * ap.quantity) DESC")
    List<Tuple> findCostProportionByMonth(
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("SELECT t.specialization, COUNT(a) " +
            "FROM Appointment a " +
            "JOIN a.technician t " +
            "GROUP BY t.specialization " +
            "ORDER BY COUNT(a) DESC")
    List<Tuple> countAppointmentsBySpecialization();


    @Query("SELECT a.id " +
            "FROM Appointment a " +
            "WHERE a.status NOT IN :resolvedOrderStatus ")
    List<Tuple> selectUnresolvedOrders(@Param("resolvedOrderStatus") List<Appointment.Status> resolvedOrderStatus);


}
