package database.cms.DTO.response;

import database.cms.entity.Feedback;

import java.util.List;

public record AllFeedbackResponse (
        List<Feedback> feedbacks
){
}
