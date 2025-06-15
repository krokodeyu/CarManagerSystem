package database.cms.repository;

import database.cms.entity.SalaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, Long> {
    List<SalaryRecord> findAll();

    @Query(value = """
        SELECT AVG(daily_salary) FROM (
            SELECT SUM(amount) AS daily_salary
            FROM salary_record
            WHERE technician_id = :technicianId
            GROUP BY DATE(created_at)
        ) AS daily_totals
        """, nativeQuery = true)
    Double findDailyAverageSalaryByTechnicianId(@Param("technicianId") Long technicianId);
}
