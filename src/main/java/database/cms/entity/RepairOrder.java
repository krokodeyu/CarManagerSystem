package database.cms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "repair_orders")
public class RepairOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_cost", nullable = false)
    private Double totalCost;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "repairOrder")
    private List<RepairItem> repairItems;

    @OneToMany(mappedBy = "repairOrder")
    private List<OrderPart> orderParts;

    @OneToMany(mappedBy = "repairOrder")
    private List<Feedback> feedbacks;

    // Getters and setters omitted for brevity
}
