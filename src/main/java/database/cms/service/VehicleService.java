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
        User user = userRepository.findByName(username)
                .orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND", "无效的用户名"));
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
    public VehicleInfoResponse generateResponse(Vehicle vehicle){
        List<Long> appointmentIds = new ArrayList<>();
        for (Appointment appointment : vehicle.getAppointments()) {
            appointmentIds.add(appointment.getId());
        }

        return new VehicleInfoResponse(
                vehicle.getUser().getId(),
                vehicle.getModel(),
                vehicle.getLicensePlate(),
                appointmentIds
        );
    }

    @Transactional(readOnly = true)
    public VehicleInfoResponse getVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));
        return generateResponse(vehicle);
    }

    @Transactional
    public VehicleChangeResponse update(ChangeVehicleRequest request,
                                        Authentication authentication) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND", "车辆不存在"));
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

    @Transactional(readOnly = true)
    public List<VehicleInfoResponse> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleInfoResponse> responses = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            responses.add(generateResponse(vehicle));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<VehicleInfoResponse> getUserVehicles(Long userId) {
        List<Vehicle> vehicles = vehicleRepository.findAllByUserId(userId);
        List<VehicleInfoResponse> responses = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            responses.add(generateResponse(vehicle));
        }
        return responses;
    }
}
