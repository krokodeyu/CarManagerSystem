package database.cms.DTO.request;

import database.cms.entity.User;
import database.cms.entity.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentRequest (
        LocalDateTime appointmentTime,
        Long vehicleId
) {

}
