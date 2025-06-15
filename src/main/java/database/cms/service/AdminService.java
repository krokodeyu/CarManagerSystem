package database.cms.service;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.detail.CustomUserDetails;
import database.cms.entity.*;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.*;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TechnicianRepository technicianRepository;
    private final SalaryRecordRepository salaryRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;
    private final FeedbackRepository feedbackRepository;

    public AdminService(UserRepository userRepository, TechnicianRepository technicianRepository, SalaryRecordRepository salaryRecordRepository, VehicleRepository vehicleRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder, AppointmentRepository appointmentRepository, FeedbackRepository feedbackRepository) {

        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
        this.salaryRecordRepository = salaryRecordRepository;
        this.vehicleRepository = vehicleRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public UserCheckResponse checkUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "没有找到该用户"));
        List<Long> vehicleIds = new ArrayList<>();
        List<Long> appointmentIds = new ArrayList<>();
        List<Long> feedbackIds = new ArrayList<>();

        List<Vehicle> vehicles = user.getVehicles();
        List<Appointment> appointments = user.getAppointments();
        List<Feedback> feedbacks = user.getFeedbacks();

        for (Vehicle vehicle : vehicles) {
            vehicleIds.add(vehicle.getId());
        }
        for (Appointment appointment : appointments) {
            appointmentIds.add(appointment.getId());
        }
        for (Feedback feedback : feedbacks) {
            feedbackIds.add(feedback.getId());
        }

        return new UserCheckResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                vehicleIds,
                appointmentIds,
                feedbackIds
        );
    }

    public TechnicianCheckResponse checkTechnician(Long technicianId) {

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND","无效的技工id"));

        List<Long> appointmentIds = new ArrayList<>();
        List<Appointment> appointments = technician.getAppointments();
        for (Appointment appointment : appointments) {
            appointmentIds.add(appointment.getId());
        }

        return new TechnicianCheckResponse(
                technician.getId(),
                technician.getName(),
                technician.getPhone(),
                technician.getSpecialization(),
                technician.getCreatedAt(),
                appointmentIds
        );
    }

    public List<SalaryRecordResponse> checkAllSalaryRecord(){

        List<SalaryRecord> records = salaryRecordRepository.findAll();
        List<SalaryRecordResponse> responses = new ArrayList<>();
        for (SalaryRecord r : records) {
            SalaryRecordResponse response = new SalaryRecordResponse(
                    r.getId(),
                    r.getTechnician().getId(),
                    r.getAmount()
            );
            responses.add(response);
        }
        return responses;

    }


    @Transactional
    public MessageResponse changePassword(Authentication auth, ChangePasswordRequest request) {
        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
        User admin = userRepository.findById(details.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("ADMIN_NOT_FOUND", "用户不存在"));
        admin.setEncryptedPassword(passwordEncoder.encode(request.password()));
        return new MessageResponse("success!");
    }

    public void payTechnician(Long technicianId, double amount){

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND","无效技工号"));

        SalaryRecord record = new SalaryRecord();
        record.setAmount(amount);
        record.setTechnician(technician);

        technician.getSalaryRecords().add(record);
        salaryRecordRepository.save(record);
    }


    public AverageRepairFeeResponse statsAverageRepairFees(String model){
        List<Appointment.Status> excludedStatuses = Arrays.asList(Appointment.Status.CANCELLED, Appointment.Status.UNACCEPTED);

        Tuple result = vehicleRepository.sumTotalCostAndCount(model, excludedStatuses);

        Double totalFee = result.get(0, Double.class);
        Long count = result.get(1, Long.class);

        if (count == 0){
            return new AverageRepairFeeResponse(0.0);
        }

        return new AverageRepairFeeResponse(totalFee / count);
    }

    public RepairFrequenciesResponse statsFrequencies(){

        List<Appointment.Status> excludedStatuses = Arrays.asList(Appointment.Status.CANCELLED, Appointment.Status.UNACCEPTED);
        List<Tuple> response = vehicleRepository.countValidAppointmentsByModel(excludedStatuses);

        return new RepairFrequenciesResponse(response);
    }

    public List<MostFrequentFailuresResponse> statsMostFrequentFailures(String model){

        List<Tuple> results = appointmentRepository.findMostFrequentFailureByModel(model, Pageable.ofSize(3));

        List<MostFrequentFailuresResponse> responses = new ArrayList<>();

        for (Tuple t : results) {
            MostFrequentFailuresResponse response = new MostFrequentFailuresResponse(
                    t.get("description", String.class),
                    t.get("count", Long.class));
            responses.add(response);
        }

        return responses;


    }

    public FeeProportionResponse statsFeeProportions(Integer year, Integer month){

        List<Tuple> responses = appointmentRepository.findCostProportionByMonth(year, month);

        return new FeeProportionResponse(responses);
    }

    public NegativeCommentOrdersResponse statsNegativeCommentOrders(){

        List<Tuple> responses = feedbackRepository.findNegativeFeedbacksWithAppointmentAndTechnician();

        return new NegativeCommentOrdersResponse(responses);
    }

    public TechnicianPerformanceResponse statsTechnicianPerformance(){

        List<Tuple> responses = appointmentRepository.countAppointmentsBySpecialization();

        return new TechnicianPerformanceResponse(responses);
    }

    public UnresolvedOrdersResponse statsUnresolvedOrders(){
        List<Appointment.Status> resolvedOrderStatus = Arrays.asList(Appointment.Status.CANCELLED, Appointment.Status.FINISHED);
        List<Tuple> responses = appointmentRepository.selectUnresolvedOrders(resolvedOrderStatus);

        return new UnresolvedOrdersResponse(responses);
    }
}
