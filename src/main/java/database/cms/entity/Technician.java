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
    private List<Reminder> reminders;

    @OneToMany(mappedBy = "technician")
    private List<SalaryRecord> salaryRecords;

    @OneToMany
    @JoinColumn(name = "notification")
    private List<Notification> notifications;


    public Technician(Long id, String name, String encryptedPassword) {
        this.id = id;
        this.name = name;
        this.encryptedPassword = encryptedPassword;
    }

    public void addReminder(Appointment appointment){
        Reminder reminder = new Reminder();
        reminder.setTechnician(this);
        reminder.setAppointment(appointment);
        reminders.add(reminder);
    }

    public Technician() {

    }
}
