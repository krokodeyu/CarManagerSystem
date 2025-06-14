package database.cms.service;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.detail.CustomUserDetails;
import database.cms.entity.SalaryRecord;
import database.cms.entity.Technician;
import database.cms.entity.User;
import database.cms.entity.Vehicle;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.*;
import org.springframework.data.domain.Pageable;
import database.cms.entity.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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

        return new UserCheckResponse(user);
    }

    public TechnicianCheckResponse checkTechnician(Long technicianId) {

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND","无效的技工id"));

        return new TechnicianCheckResponse(technician);
    }

    public SalaryRecordResponse checkAllSalaryRecord(){

        List<SalaryRecord> records = salaryRecordRepository.findAll();

        return new SalaryRecordResponse(records);
    }

    public VehicleCheckResponse checkVehicle(Long vehicleId){

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()-> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "无效的车辆id"));

        return new VehicleCheckResponse(vehicle);
    }

    public OrderCheckResponse checkOrder(Long orderId){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("ORDER_NOT_FOUND", "无效订单号"));

        return new OrderCheckResponse(order);
    }

    public MaintenanceRecordResponse checkAllMaintenanceRecord(){

        List<Order> orders = orderRepository.findAll();

        return new MaintenanceRecordResponse(orders);
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

        Object[] result = vehicleRepository.sumTotalCostAndCount(model);

        double totalFee = (Double) result[0];
        Long count = (Long) result[1];

        return new AverageRepairFeeResponse(totalFee / count);
    }

    public RepairFrequenciesResponse statsFrequencies(){

        List<Object[]> response = vehicleRepository.countValidAppointmentsByModel();

        return new RepairFrequenciesResponse(response);
    }

    public MostFrequentFailuresResponse statsMostFrequentFailures(String model){

        List<Object[]> responses = appointmentRepository.findMostFrequentFailureByModel(model, Pageable.ofSize(3));

        return new MostFrequentFailuresResponse(responses.stream()
                .map(response -> (String) response[0])  // 提取 description
                .collect(Collectors.toList()));
    }

    public FeeProportionResponse statsFeeProportions(Integer year, Integer month){

        List<Object[]> responses = appointmentRepository.findCostProportionByMonth(year, month);

        return new FeeProportionResponse(responses);
    }

    public NegativeCommentOrdersResponse statsNegativeCommentOrders(){

        List<Object[]> responses = feedbackRepository.findNegativeFeedbacksWithAppointmentAndTechnician();

        return new NegativeCommentOrdersResponse(responses);
    }

    public TechnicianPerformanceResponse statsTechnicianPerformance(){

        List<Object[]> responses = appointmentRepository.countAppointmentsBySpecialization();

        return new TechnicianPerformanceResponse(responses);
    }

    public UnresolvedOrdersResponse statsUnresolvedOrders(){

        List<Object[]> responses = appointmentRepository.selectUnresolvedOrders();

        return new UnresolvedOrdersResponse(responses);
    }
}
