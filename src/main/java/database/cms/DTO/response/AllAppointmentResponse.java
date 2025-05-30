package database.cms.DTO.response;

import database.cms.entity.Appointment;

import java.util.List;

public record AllAppointmentResponse (
        List<String> appointmentIds
) {

}
