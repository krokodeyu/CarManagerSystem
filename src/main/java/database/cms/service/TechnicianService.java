package database.cms.service;

import database.cms.DTO.request.TechRegisterRequest;
import database.cms.DTO.request.TechUpdateRequest;
import database.cms.DTO.response.ReminderResponse;
import database.cms.DTO.response.TechnicianInfoResponse;
import database.cms.entity.Reminder;
import database.cms.entity.TechSpec;
import database.cms.entity.Technician;
import database.cms.exception.AuthErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.TechnicianRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    private final PasswordEncoder passwordEncoder;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
        passwordEncoder = new BCryptPasswordEncoder();
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
        Technician tech = (Technician) authentication.getPrincipal();
        List<ReminderResponse> reminders = new ArrayList<>();
        for(Reminder reminder : tech.getReminders()) {
            reminders.add(generateReminderResponse(reminder));
        }
        return reminders;
    }
}
