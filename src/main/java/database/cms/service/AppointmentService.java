package database.cms.service;

import database.cms.DTO.request.AppointmentCancelRequest;
import database.cms.DTO.request.AppointmentConfirmationRequest;
import database.cms.DTO.request.AppointmentRequest;
import database.cms.DTO.request.AppointmentRevisionRequest;
import database.cms.DTO.response.*;
import database.cms.entity.Appointment;
import database.cms.entity.User;
import database.cms.entity.Vehicle;
import database.cms.event.AppointmentCreateEvent;
import database.cms.exception.BusinessErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.AppointmentRepository;
import database.cms.repository.UserRepository;
import database.cms.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, VehicleRepository vehicleRepository, ApplicationEventPublisher applicationEventPublisher){

        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public String generateAppointmentId(){
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

    public AppointmentResponse createAppointment(AppointmentRequest request){

        User user = userRepository.findById(request.userId())
                .orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND", "用户不存在"));

        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(()-> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));

        Appointment appointment = new Appointment(
                null,
                 user,
                 vehicle,
                null,
                 Appointment.Status.UNACCEPTED
        );

        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setAppointmentId(generateAppointmentId());
        appointmentRepository.save(appointment);

        return new AppointmentResponse(
                appointment.getId(),
                user.getId(),
                vehicle.getId(),
                null,
                appointment.getAppointmentId(),
                Appointment.Status.UNACCEPTED,
                appointment.getAppointmentTime(),
                appointment.getCreatedAt(),
                LocalDateTime.now()
        );
    }
    // Authorization?
    public AllAppointmentResponse getAllAppointment(){

        List<Appointment> appointmentList = appointmentRepository.findAll();

        List<String> appointmentIds = appointmentList.stream().map(Appointment::getAppointmentId).collect(Collectors.toList());

        return new AllAppointmentResponse(appointmentIds);
    }

    public AppointmentDetailResponse getAppointmentDetail(Long appointmentId){

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "无效订单号"));

        return new AppointmentDetailResponse(appointment);
    }


    public AppointmentRevisionResponse reviseAppointment(AppointmentRevisionRequest request){

        Appointment appointment = appointmentRepository.findById(request.targetId())
                .orElseThrow(()-> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "无效订单号"));

        request.revisedAppointment().setId(appointment.getId());
        request.revisedAppointment().setUser(appointment.getUser());
        request.revisedAppointment().setCreatedAt(appointment.getCreatedAt());

        AppointmentCreateEvent event = AppointmentCreateEvent.event(this, appointment);
        applicationEventPublisher.publishEvent(event);
        //appointmentRepository.save(request.revisedAppointment());

        return new AppointmentRevisionResponse(true);
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

    public AppointmentConfirmationResponse confirmAppointment(AppointmentConfirmationRequest request){

        Appointment existingAppointment = appointmentRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "无效订单号"));

        if (existingAppointment.getStatus() != Appointment.Status.UNACCEPTED) {
            throw new BusinessErrorException("INVALID_STATUS", "当前状态无法确认预约");
        }

        existingAppointment.setStatus(Appointment.Status.ONGOING);

        appointmentRepository.save(existingAppointment);

        return new AppointmentConfirmationResponse(true, request.id()); // 模拟订单号
    }

}
