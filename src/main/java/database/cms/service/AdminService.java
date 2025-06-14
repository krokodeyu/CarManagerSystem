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
import database.cms.entity.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TechnicianRepository technicianRepository;
    private final SalaryRecordRepository salaryRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, TechnicianRepository technicianRepository, SalaryRecordRepository salaryRecordRepository, VehicleRepository vehicleRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.technicianRepository = technicianRepository;
        this.salaryRecordRepository = salaryRecordRepository;
        this.vehicleRepository = vehicleRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
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
}
