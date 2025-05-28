package database.cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import database.cms.DTO.request.AddVehicleRequest;
import database.cms.DTO.request.ChangeVehicleRequest;
import database.cms.DTO.response.MessageResponse;
import database.cms.DTO.response.VehicleChangeResponse;
import database.cms.DTO.response.VehicleInfoResponse;
import database.cms.entity.*;
import database.cms.exception.AuthErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.UserRepository;
import database.cms.repository.VehicleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public VehicleChangeResponse addVehicle(AddVehicleRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new ResourceNotFoundException("USER_NOT_FOUND", "未找到用户");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setUser(user);
        vehicle.setModel(request.model());
        vehicle.setLicensePlate(request.licensePlate());

        user.getVehicles().add(vehicle);
        userRepository.save(user); // 自动存储vehicle

        return new VehicleChangeResponse(
                vehicle.getId(),
                vehicle.getModel(),
                vehicle.getLicensePlate()
        );
    }

    @Transactional(readOnly = true)
    public VehicleInfoResponse getVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));
        List<Long> orderIds = new ArrayList<>();
        for (RepairOrder order : vehicle.getRepairOrders()) {
            orderIds.add(order.getId());
        }
        List<Long> appointmentIds = new ArrayList<>();
        for (Appointment appointment : vehicle.getAppointments()) {
            appointmentIds.add(appointment.getId());
        }

        return new VehicleInfoResponse(
                vehicle.getUser().getId(),
                vehicle.getModel(),
                vehicle.getLicensePlate(),
                orderIds,
                appointmentIds
        );
    }

    @Transactional
    public VehicleChangeResponse update(ChangeVehicleRequest request,
                                        Authentication authentication) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if(!role.equals("ROLE_ADMIN")) {
            User user = userRepository.findByName(authentication.getName());
            if(!Objects.equals(vehicle.getUser().getId(), user.getId())) {
                throw new AuthErrorException("NOT_USER_PROPERTY", "车辆不属于用户");
            }
        }
        vehicle.setModel(request.model());
        vehicle.setLicensePlate(request.licensePlate());

        return new VehicleChangeResponse(
                vehicle.getId(),
                vehicle.getModel(),
                vehicle.getLicensePlate()
        );
    }

    @Transactional
    public MessageResponse delete(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));
        vehicleRepository.delete(vehicle);
        return new MessageResponse("成功删除");
    }
}
