package database.cms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "technicians")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.TECH;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @OneToMany(mappedBy = "technician")
    private List<RepairOrder> repairOrders;

    @OneToMany(mappedBy = "technician")
    private List<Appointment> appointments;

}
