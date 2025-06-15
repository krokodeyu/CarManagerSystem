package database.cms.DTO.response;

import database.cms.entity.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponse (
        Long id,
        String appointmentId,
        Long userId,
        Long vehicleId,
        Long technicianId,
        Appointment.Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

){
}
