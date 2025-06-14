package database.cms.DTO.request;

public record AppointmentPartAddRequest(
        Long appointmentId,
        Long partId,
        Integer quantity
) {
}
