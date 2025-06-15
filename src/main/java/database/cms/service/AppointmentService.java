package database.cms.service;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.detail.CustomUserDetails;
import database.cms.entity.*;
import database.cms.entity.Appointment;
import database.cms.entity.Technician;
import database.cms.entity.User;
import database.cms.entity.Vehicle;
import database.cms.exception.BusinessErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.*;
import database.cms.repository.AppointmentRepository;
import database.cms.repository.TechnicianRepository;
import database.cms.repository.UserRepository;
import database.cms.repository.VehicleRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final TechnicianRepository technicianRepository;
    private final NotificationRepository notificationRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, VehicleRepository vehicleRepository, TechnicianRepository technicianRepository, NotificationRepository notificationRepository){
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.technicianRepository = technicianRepository;
        this.notificationRepository = notificationRepository;
    }

    private String generateAppointmentId(){
        String appointmentId;

        do {
            UUID uuid = UUID.randomUUID();
            appointmentId = uuid.toString()
                    .replace("-", "")  // 移除连字符
                    .substring(0, 16)  // 截取前16位
                    .toUpperCase();
        } while (appointmentRepository.existsByAppointmentId(appointmentId));

        return appointmentId;
    }

    public AppointmentResponse createAppointment(AppointmentRequest request, Authentication authentication){
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        Long userId = details.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND", "用户不存在"));

        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(()-> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setVehicle(vehicle);
        appointment.setStatus(Appointment.Status.UNACCEPTED);
        appointment.setCreatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return new AppointmentResponse(
                appointment.getId(),
                generateAppointmentId(),
                user.getId(),
                vehicle.getId(),
                null,
                Appointment.Status.UNACCEPTED,
                appointment.getCreatedAt(),
                LocalDateTime.now()
        );
    }
    // Authorization?
    public List<AppointmentResponse> getAllAppointment(){

        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponse response = new AppointmentResponse(
                    appointment.getId(),
                    appointment.getAppointmentId(),
                    appointment.getUser().getId(),
                    appointment.getVehicle().getId(),
                    appointment.getTechnician().getId(),
                    appointment.getStatus(),
                    appointment.getCreatedAt(),
                    appointment.getUpdatedAt()
            );
            appointmentResponses.add(response);
        }
        return appointmentResponses;
    }

    public AppointmentResponse getAppointmentDetail(Long appointmentId){

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "无效订单号"));

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentId(),
                appointment.getUser().getId(),
                appointment.getVehicle().getId(),
                appointment.getTechnician().getId(),
                appointment.getStatus(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }


    public AppointmentCancelResponse cancelAppointment(AppointmentCancelRequest request){

        Appointment existingAppointment = appointmentRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "无效订单号"));

        if (existingAppointment.getStatus() == Appointment.Status.CANCELLED) {
            throw new BusinessErrorException("ALREADY_CANCELLED", "预约已取消，无需重复操作");
        }

        existingAppointment.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(existingAppointment);

        return new AppointmentCancelResponse(true);
    }

    public AppointmentConfirmationResponse confirmAppointment(AppointmentConfirmationRequest request, Authentication authentication){
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        Long techId = details.getId();
        User admin = userRepository.findAdmin();

        Appointment existingAppointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "无效订单号"));

        if (existingAppointment.getStatus() != Appointment.Status.UNACCEPTED) {
            throw new BusinessErrorException("INVALID_STATUS", "当前状态无法确认预约");
        }

        Notification notification = new Notification();
        notification.setAppointmentId(existingAppointment.getId());

        if (request.confirm()) {
            Technician technician = technicianRepository.findById(techId)
                    .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND","未找到技工"));

            existingAppointment.setStatus(Appointment.Status.ONGOING);
            existingAppointment.setTechnician(technician);
            appointmentRepository.save(existingAppointment);

            notification.setContent("The technician has confirmed the appointment!");
            notificationRepository.save(notification);
            admin.getNotification().add(notification);

            return new AppointmentConfirmationResponse(true, request.appointmentId());
        } else {
            notification.setContent("The technician rejected the appointment!");
            notificationRepository.save(notification);
            admin.getNotification().add(notification);

            return new AppointmentConfirmationResponse(false, request.appointmentId());
        }
    }

    @Transactional
    public MessageResponse remind(Long orderId) {
        Appointment app = appointmentRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", "未找到订单"));
        Technician tech = app.getTechnician();
        Notification n = tech.addNotification(app);
        technicianRepository.save(tech);
        notificationRepository.save(n);
        return new MessageResponse("success");
    }

    public AppointmentByStatusResponse getAppointmentByStatus(Appointment.Status status){

        List<Object[]> results = appointmentRepository.findAppointmentsByStatus(status);

        List<Long> appointmentIds = new ArrayList<>();

        for (Object[] result : results) {
            appointmentIds.add((Long) result[0]);
        }

        return new AppointmentByStatusResponse(appointmentIds);
    }

    public void arrangeAppointment(AppointmentArrangementRequest request){

        Technician technician = technicianRepository.findById(request.technicianId())
                .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND","无效技工名称"));

        Notification notification = new Notification();
        notification.setAppointmentId(request.appointmentId());
        notification.setTechnician(technician);
        notification.setContent("You have new appointments!");
        notificationRepository.save(notification);

        technician.getNotifications().add(notification);
    }

    public AppointmentFinishResponse finishAppointment(AppointmentFinishRequest request){
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND","无效的订单号"));

        User user = appointment.getUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Technician technician = technicianRepository.findByName(currentUsername);

        double hourlyWage = TechSpec.HOURLY_WAGE_MAP(technician.getSpecialization());
        double totalCost = hourlyWage * request.totalHours();

        appointment.setStatus(Appointment.Status.FINISHED);
        appointment.setTotalHours(request.totalHours());
        appointment.setTotalCost(totalCost);
        appointmentRepository.save(appointment);

        Notification notification = new Notification();
        notification.setAppointmentId(request.appointmentId());
        notification.setUser(user);
        notification.setContent("Your appointment has been finished!");
        notificationRepository.save(notification);

        user.getNotification().add(notification);

        return new AppointmentFinishResponse(request.appointmentId(), request.totalHours());

    }
}
