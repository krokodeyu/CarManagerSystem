package database.cms.service;

import database.cms.DTO.request.FeedbackSubmitRequest;
import database.cms.DTO.response.FeedbackResponse;
import database.cms.DTO.response.FeedbackCheckResponse;
import database.cms.DTO.response.MessageResponse;
import database.cms.entity.Appointment;
import database.cms.entity.Feedback;
import database.cms.entity.User;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.AppointmentRepository;
import database.cms.repository.FeedbackRepository;
import database.cms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public MessageResponse submitFeedback(FeedbackSubmitRequest request) {
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
        feedbackRepository.save(feedback);

        return new MessageResponse("success");
    }

    public List<FeedbackResponse> getAllFeedback() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackResponse> feedbackResponses = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponse response = new FeedbackResponse(
                    feedback.getId(),
                    feedback.getUser().getId(),
                    feedback.getAppointment().getId(),
                    feedback.getRating(),
                    feedback.getComment(),
                    feedback.getCreatedAt()
            );
        }
        return feedbackResponses;

    }

    public FeedbackCheckResponse getFeedbackCheck(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "订单不存在"));

        if (appointment.getFeedbacks().isEmpty()) return null;

        return new FeedbackCheckResponse(appointment.getFeedbacks());
    }

}
