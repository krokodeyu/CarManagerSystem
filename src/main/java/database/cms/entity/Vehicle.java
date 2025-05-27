package database.cms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "vehicle")
    private List<RepairOrder> repairOrders;

    @OneToMany(mappedBy = "vehicle")
    private List<Appointment> appointments;

}
