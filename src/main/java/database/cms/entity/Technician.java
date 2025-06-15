package database.cms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(name = "role", nullable = false)
    private Role role = Role.TECH;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private TechSpec specialization;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @OneToMany(mappedBy = "technician")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "technician")
    private List<SalaryRecord> salaryRecords;

    @OneToMany(mappedBy = "technician")
    private List<Notification> notifications;


    public Technician(Long id, String name, String encryptedPassword) {
        this.id = id;
        this.name = name;
        this.encryptedPassword = encryptedPassword;
    }

    public Notification addNotification(Appointment appointment){
        Notification n = new Notification();
        n.setIsRead(false);
        n.setTechnician(this);
        n.setUser(appointment.getUser());
        n.setAppointmentId(appointment.getId());
        n.setContent("用户催单，单号" + appointment.getAppointmentId());
        notifications.add(n);
        return n;
    }

    public Technician() {

    }

    public List<Notification> getNotification() {return notifications;}
}
