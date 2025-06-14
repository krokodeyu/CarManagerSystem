package database.cms.DTO.request;

public record AppointmentFinishRequest(
        Long appointmentId,
        Integer totalHours
) {
}
