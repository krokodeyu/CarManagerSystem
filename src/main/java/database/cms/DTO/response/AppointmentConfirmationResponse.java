package database.cms.DTO.response;

public record AppointmentConfirmationResponse (
        Boolean success,
        Long appointmentId
) {
}
