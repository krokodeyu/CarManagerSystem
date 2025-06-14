package database.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    public Appointment() {

    }

    public enum Status {
        UNACCEPTED,
        ONGOING,
        CANCELLED,
        FINISHED,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id")
    private String appointmentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.UNACCEPTED;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.REMOVE)
    private List<RepairItem> repairItems;

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentPart> appointmentParts;

    @OneToMany(mappedBy = "appointment")
    private List<Feedback> feedbacks;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "total_cost")
    private Double totalCost;


    public Appointment(Long id, User user, Vehicle vehicle, Technician technician, Status status) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.technician = technician;
        this.status = status;
    }
    // Getters and setters omitted for brevity
}
