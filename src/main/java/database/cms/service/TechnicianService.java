package database.cms.service;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.detail.CustomUserDetails;
import database.cms.entity.*;
import database.cms.exception.AuthErrorException;
import database.cms.exception.BusinessErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final RepairItemRepository repairItemRepository;
    private final AppointmentRepository appointmentRepository;
    private final SparePartRepository sparePartRepository;
    private final SalaryRecordRepository salaryRecordRepository;

    public TechnicianService(TechnicianRepository technicianRepository, NotificationRepository notificationRepository, RepairItemRepository repairItemRepository, AppointmentRepository appointmentRepository, SparePartRepository sparePartRepository, SalaryRecordRepository salaryRecordRepository) {
        this.technicianRepository = technicianRepository;
        this.notificationRepository = notificationRepository;
        this.repairItemRepository = repairItemRepository;
        this.sparePartRepository = sparePartRepository;
        passwordEncoder = new BCryptPasswordEncoder();
        this.appointmentRepository = appointmentRepository;
        this.salaryRecordRepository = salaryRecordRepository;
    }

    @Transactional(readOnly = true)
    public TechnicianInfoResponse generateResponse(Technician tech) {
        return new TechnicianInfoResponse(
                null,
                tech.getName(),
                tech.getPhone(),
                tech.getSpecialization().getString(),
                tech.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public TechnicianInfoResponse getInfo(Long technicianId) {
        Technician tech = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("TECH_NOT_FOUND", "维修员不存在"));
        return generateResponse(tech);
    }

    @Transactional(readOnly = true)
    public List<TechnicianInfoResponse> getAllInfo() {
        List<Technician> technicians = technicianRepository.findAll();
        List<TechnicianInfoResponse> technicianInfoResponses = new ArrayList<>();
        for(Technician tech : technicians) {
            technicianInfoResponses.add(generateResponse(tech));
        }
        return technicianInfoResponses;
    }

    @Transactional
    public TechnicianInfoResponse register(TechRegisterRequest request) {
        if(technicianRepository.existsByPhone(request.phone())) {
            throw new AuthErrorException("PHONE_ALREADY_EXISTS", "电话号已被注册");
        }

        String name = request.name();
        String phone = request.phone();
        TechSpec spec = request.techSpec();
        String encryptedPassword = passwordEncoder.encode(request.password());

        Technician tech = new Technician();

        tech.setName(name);
        tech.setPhone(phone);
        tech.setSpecialization(spec);
        tech.setEncryptedPassword(encryptedPassword);
        technicianRepository.save(tech);

        return new TechnicianInfoResponse(
                tech.getId(),
                name,
                phone,
                spec.getString(),
                tech.getCreatedAt()
        );
    }

    @Transactional
    public TechnicianInfoResponse update(Long technicianId, TechUpdateRequest request) {
        Technician tech = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("TECH_NOT_FOUND", "未找到修理人员"));
        tech.setPhone(request.phone());
        tech.setSpecialization(request.techSpec());
        String encryptedPassword = passwordEncoder.encode(request.password());
        tech.setEncryptedPassword(encryptedPassword);
        return new TechnicianInfoResponse(
                tech.getId(),
                tech.getName(),
                tech.getPhone(),
                tech.getSpecialization().getString(),
                tech.getCreatedAt()
        );
    }

    private ReminderResponse generateReminderResponse(Reminder reminder){
        return new ReminderResponse(
                reminder.getId(),
                reminder.getAppointment().getId(),
                reminder.getTechnician().getId()
        );
    }

    @Transactional(readOnly = true)
    public List<ReminderResponse> checkReminders(Authentication authentication) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        Technician tech = technicianRepository.findById(details.getId())
                .orElseThrow(() -> new ResourceNotFoundException("TECH_NOT_FOUND", "未找到修理人员"));
        List<ReminderResponse> reminders = new ArrayList<>();
        for(Reminder reminder : tech.getReminders()) {
            reminders.add(generateReminderResponse(reminder));
        }
        return reminders;
    }

    public List<RepairItemCheckResponse> checkRepairItem(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "未找到订单"));

        List<RepairItemCheckResponse> responses = new ArrayList<>();

        for(RepairItem repairItem : appointment.getRepairItems()) {
            RepairItemCheckResponse response = new RepairItemCheckResponse(repairItem.getDescription());
            responses.add(response);
        }

        return responses;

    }

    @Transactional
    public MessageResponse addRepairItems(RepairItemsAddRequest request) {
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "未找到订单"));

        RepairItem repairItem = new RepairItem();
        repairItem.setAppointment(appointment);
        repairItem.setDescription(request.Description());
        repairItem.setCost(request.cost());
        repairItemRepository.save(repairItem);

        appointment.getRepairItems().add(repairItem);

        return new MessageResponse("新的维修项目已添加!");
    }

    public MessageResponse deleteRepairItems(RepairItemsDeleteRequest request) {
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "未找到订单"));

        RepairItem repairItem = repairItemRepository.findById(request.repairItemId())
                .orElseThrow(()-> new ResourceNotFoundException("ITEM_NOT_FOUND","未找到订单配件"));

        appointment.getRepairItems().remove(repairItem);

        return new MessageResponse("该维修项目已删除！");
    }

    public List<AppointmentPartsCheckResponse> checkAppointmentParts(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "未找到订单"));

        List<AppointmentPartsCheckResponse> responses = new ArrayList<>();

        for (AppointmentPart appointmentPart : appointment.getAppointmentParts()) {
            AppointmentPartsCheckResponse response = new AppointmentPartsCheckResponse(
                    appointmentPart.getId(), appointmentPart.getSparePart().getName());
            responses.add(response);
        }

        return responses;
    }

    public MessageResponse addAppointmentPart(AppointmentPartAddRequest request) {

        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "未找到订单"));

        SparePart sparePart = sparePartRepository.findById(request.partId())
                .orElseThrow(()-> new ResourceNotFoundException("PART_NOT_FOUND","配件未找到"));

        if(request.quantity() > sparePart.getQuantity()) {
            throw new BusinessErrorException("LOW_STOCK", "配件数量不足");
        }

        AppointmentPart appointmentPart = new AppointmentPart();
        appointmentPart.setSparePart(sparePart);
        appointmentPart.setQuantity(request.quantity());
        appointmentPart.setAppointment(appointment);
        appointmentPart.setUnitPrice(sparePart.getPrice());

        appointment.getAppointmentParts().add(appointmentPart);

        return new MessageResponse("订单配件添加成功！");
    }

    public MessageResponse deleteAppointmentPart(AppointmentPartDeleteRequest request) {
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("APPOINTMENT_NOT_FOUND", "未找到订单"));

        AppointmentPart appointmentPart = appointment.getAppointmentParts().stream()
                .filter(part -> part.getId().equals(request.PartId()))
                .findFirst().orElse(null);

        appointment.getAppointmentParts().remove(appointmentPart);

        return new MessageResponse("已删除订单配件！");
    }

    public AvgSalaryResponse calculateAvgSalary(Authentication authentication) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        Long techId = details.getId();
        Double avgSalary = salaryRecordRepository.findDailyAverageSalaryByTechnicianId(techId);
        return new AvgSalaryResponse(avgSalary);
    }
}
/*
* GET	/orders/{id}/items	查询订单维修项目	all
POST	/orders/{id}/items	添加维修项目	tech
DELETE	/orders/{id}/items/{itemId}	删去维修项目	tech
GET	/orders/{id}/parts	查询订单所用配件	all
POST	/orders/{id}/parts	添加订单配件	tech
DELETE	orders/{id}/parts/{partId}	删去订单配件	tech*/