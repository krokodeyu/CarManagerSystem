package database.cms.DTO.response;

import database.cms.entity.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponse (
        Long id,
        Long userId,
        Long vehicleId,
        Long technicianId,
        String appointmentId,
        Appointment.Status status,
        LocalDateTime appointmentTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

){
}
