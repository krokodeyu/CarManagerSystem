package database.cms.DTO.response;

import database.cms.entity.Feedback;

import java.util.List;

public record NegativeFeedbackResponse (
        List<Feedback> feedbacks
){
}
