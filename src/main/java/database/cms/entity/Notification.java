package database.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user", nullable = true)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "technician", nullable = true)
    private Technician technician;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    public Notification() {

    }
}
