package database.cms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "salary_record")
@Data
public class SalaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "technician_id", nullable = false)
    private Technician technician;*/
}
