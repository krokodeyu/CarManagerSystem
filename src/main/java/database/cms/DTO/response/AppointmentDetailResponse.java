package database.cms.DTO.response;

import database.cms.entity.Appointment;

public record AppointmentDetailResponse (
    Appointment appointment
){
}
