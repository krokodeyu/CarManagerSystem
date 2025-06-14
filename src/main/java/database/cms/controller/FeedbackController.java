package database.cms.controller;

import database.cms.DTO.request.FeedbackSubmitRequest;
import database.cms.DTO.response.AllFeedbackResponse;
import database.cms.DTO.response.FeedbackCheckResponse;
import database.cms.DTO.response.NegativeFeedbackResponse;
import database.cms.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping()
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackSubmitRequest request) {
        feedbackService.submitFeedback(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<AllFeedbackResponse> getAllFeedback() {
        AllFeedbackResponse response = feedbackService.getAllFeedback();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<FeedbackCheckResponse> checkFeedback(@PathVariable Long appointmentId) {
        FeedbackCheckResponse response = feedbackService.getFeedbackCheck(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/negative/{appointmentId}")
    public ResponseEntity<NegativeFeedbackResponse> negativeFeedback(@PathVariable Long appointmentId) {
        NegativeFeedbackResponse response = feedbackService.getNegativeFeedback(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
