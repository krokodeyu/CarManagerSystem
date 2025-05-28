package database.cms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@DynamicUpdate
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean expired = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<RepairOrder> repairOrders;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

}
