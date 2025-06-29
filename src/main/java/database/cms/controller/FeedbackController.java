package database.cms.controller;

import database.cms.DTO.request.FeedbackSubmitRequest;
import database.cms.DTO.response.FeedbackResponse;
import database.cms.DTO.response.FeedbackCheckResponse;
import database.cms.DTO.response.MessageResponse;
import database.cms.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
方法	路径	描述	权限
POST	/feedbacks	提交评价（用户）	user
GET	/feedbacks	所有评价（管理员）	admin
GET	/feedbacks/order/{orderId}	查看某订单评价	all
GET	/feedbacks/negative	查询差评记录	all
*/


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }


    @PostMapping
    public ResponseEntity<MessageResponse> submitFeedback(@RequestBody FeedbackSubmitRequest request) {
        MessageResponse response = feedbackService.submitFeedback(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        List<FeedbackResponse> response = feedbackService.getAllFeedback();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<FeedbackCheckResponse> checkFeedback(@PathVariable Long appointmentId) {
        FeedbackCheckResponse response = feedbackService.getFeedbackCheck(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
