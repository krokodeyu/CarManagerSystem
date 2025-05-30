package database.cms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "vehicle")
    private List<Appointment> appointments;


    public Vehicle(Long id, User user, String make, String model, String licensePlate) {
        this.id = id;
        this.user = user;
        this.make = make;
        this.model = model;
        this.licensePlate = licensePlate;
    }

    public Vehicle() {

    }
    // Getters and setters omitted for brevity
}
