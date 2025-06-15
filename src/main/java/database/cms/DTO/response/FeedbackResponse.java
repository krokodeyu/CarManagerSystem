package database.cms.DTO.response;

import database.cms.entity.Feedback;

import java.time.LocalDateTime;
import java.util.List;

public record FeedbackResponse(
        Long feedbackId,
        Long userId,
        Long appointmentId,
        Integer rating,
        String comment,
        LocalDateTime createdAt

){
}
