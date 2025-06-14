package database.cms.DTO.request;

public record FeedbackSubmitRequest (
        Long userId,
        Long appointmentId,
        int rating,
        String comment
){
}
