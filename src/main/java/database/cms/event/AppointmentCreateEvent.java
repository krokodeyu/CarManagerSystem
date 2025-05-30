package database.cms.event;

import database.cms.entity.Appointment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AppointmentCreateEvent extends ApplicationEvent {

    private final Appointment appointment;

    private AppointmentCreateEvent(Object source, Appointment appointment) {
        super(source);
        this.appointment = appointment;
    }

    public static AppointmentCreateEvent event(Object source, Appointment appointment) {
        return new AppointmentCreateEvent(source, appointment);
    }
}
