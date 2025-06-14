package database.cms.DTO.request;

public record AppointmentPartDeleteRequest(
        Long appointmentId,
        Long PartId
) {
}
