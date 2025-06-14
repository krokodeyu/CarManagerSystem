package database.cms.service;

import database.cms.DTO.request.FeedbackSubmitRequest;
import database.cms.DTO.response.AllFeedbackResponse;
import database.cms.DTO.response.FeedbackCheckResponse;
import database.cms.DTO.response.NegativeFeedbackResponse;
import database.cms.entity.Appointment;
import database.cms.entity.Feedback;
import database.cms.entity.User;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.AppointmentRepository;
import database.cms.repository.FeedbackRepository;
import database.cms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
方法	路径	描述	权限
POST	/feedbacks	提交评价（用户）	user
GET	/feedbacks	所有评价（管理员）	admin
GET	/feedbacks/order/{orderId}	查看某订单评价	all
GET	/feedbacks/negative	查询某个订单差评记录	all
*/

@Service
public class FeedbackService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final FeedbackRepository feedbackRepository;


    public FeedbackService(UserRepository userRepository, AppointmentRepository appointmentRepository, FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public void submitFeedback(FeedbackSubmitRequest request) {
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "订单不存在"));
        User user = userRepository.findById(request.userId())
                .orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND", "用户不存在"));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setAppointment(appointment);
        feedback.setRating(request.rating());
        feedback.setComment(request.comment());

        appointment.getFeedbacks().add(feedback);
        appointmentRepository.save(appointment);

    }

    public AllFeedbackResponse getAllFeedback() {
        return new AllFeedbackResponse(feedbackRepository.findAll());
    }

    public FeedbackCheckResponse getFeedbackCheck(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "订单不存在"));

        if (appointment.getFeedbacks().isEmpty()) return null;

        return new FeedbackCheckResponse(appointment.getFeedbacks());
    }

    public NegativeFeedbackResponse getNegativeFeedback(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "订单不存在"));

        if (appointment.getFeedbacks().isEmpty()) return null;

        List<Feedback> feedbacks = new ArrayList<>();
        for (Feedback feedback : appointment.getFeedbacks()) {
            if (feedback.getRating() - 3 < 1e-4) feedbacks.add(feedback);
        }

        return new NegativeFeedbackResponse(feedbacks);
    }
}
