package database.cms.DTO.response;

public record AppointmentFinishResponse(
        Long appointmentId,
        Integer totalHours
) {
}
