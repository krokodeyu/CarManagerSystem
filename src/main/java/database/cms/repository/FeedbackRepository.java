package database.cms.repository;

import database.cms.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAll();

    @Query("SELECT f, a, t " +
            "FROM Feedback f " +
            "JOIN f.appointment a " +          // 关联 Appointment
            "JOIN a.technician t " +           // 关联 Technician
            "WHERE f.rating < 3")               // 筛选条件
    List<Object[]> findNegativeFeedbacksWithAppointmentAndTechnician();
}




