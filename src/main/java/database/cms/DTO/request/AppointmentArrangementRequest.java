package database.cms.DTO.request;

public record AppointmentArrangementRequest(
        Long appointmentId,
        Long technicianId
) {
}
