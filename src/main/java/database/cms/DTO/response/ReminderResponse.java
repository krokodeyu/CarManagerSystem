package database.cms.DTO.response;

public record ReminderResponse(
        Long id,
        Long appointmentId,
        Long techId
) {
}
