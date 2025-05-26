package database.cms.controller;

import database.cms.service.FeedbackService;
import org.springframework.stereotype.Controller;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
}
