package database.cms.service;

import database.cms.DTO.request.OrderCheckRequest;
import database.cms.DTO.request.TechnicianCheckRequest;
import database.cms.DTO.request.UserCheckRequest;
import database.cms.DTO.request.VehicleCheckRequest;
import database.cms.DTO.response.*;
import database.cms.entity.*;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TechnicianRepository technicianRepository;
    private final SalaryRecordRepository salaryRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final FeedbackRepository feedbackRepository;

    public AdminService(UserRepository userRepository, TechnicianRepository technicianRepository, SalaryRecordRepository salaryRecordRepository, VehicleRepository vehicleRepository, OrderRepository orderRepository, AppointmentRepository appointmentRepository, FeedbackRepository feedbackRepository) {

        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
        this.salaryRecordRepository = salaryRecordRepository;
        this.vehicleRepository = vehicleRepository;
        this.orderRepository = orderRepository;
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

    public void payTechnician(Long technicianId, double amount){

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(()-> new ResourceNotFoundException("TECH_NOT_FOUND","无效技工号"));

        SalaryRecord record = new SalaryRecord();
        record.setAmount(amount);
        record.setTechnician(technician);

        technician.setSalaryRecord(record);
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


/*

    @GetMapping("/stats/unresolved-orders")
    public ResponseEntity<?> statsUnresolvedOrders() {
        List<?> response = adminService.statsUnresolvedOrders();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
* */
/*
方法	路径	描述	权限
GET	/stats/vehicle-repairs	按车型统计维修次数	admin
GET	/stats/average-repair-fees	按车型同济平均维修费用	admin
GET	/stats/repair-frequencies	所有车型的维修频率	admin
GET	/stats/most-frequent-failures	特定车型最长出现的故障	admin
GET	/stats/fee-proportions	按月份统计维修费用构成	admin
GET	/stats/negative-comment-orders	筛选负面反馈工单及涉及员工	admin
GET	/stats/costs	月/季度维修费用分析	admin
GET	/stats/technician-performance	不同工种维修量统计	admin
GET	/stats/unresolved-orders	当前未完成订单列表	admin
*/