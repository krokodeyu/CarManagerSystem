package database.cms.DTO.request;

import database.cms.entity.Appointment;

public record AppointmentRevisionRequest (
        Long targetId,
        Appointment revisedAppointment
){
}
