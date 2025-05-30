package database.cms.repository;

import database.cms.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll();

    Optional<Appointment> findById(long id);

    Boolean existsByAppointmentId(String appointmentId);

}
