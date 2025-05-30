package database.cms.service;

import java.util.ArrayList;
import java.util.List;

import database.cms.DTO.request.AddVehicleRequest;
import database.cms.DTO.response.AddVehicleResponse;
import database.cms.DTO.response.VehicleResponse;
import database.cms.entity.Appointment;
import database.cms.entity.RepairOrder;
import database.cms.entity.User;
import database.cms.entity.Vehicle;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.UserRepository;
import database.cms.repository.VehicleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public AddVehicleResponse addVehicle(AddVehicleRequest request, Authentication authentication) {
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

        return new AddVehicleResponse(
                vehicle.getId(),
                vehicle.getModel(),
                vehicle.getLicensePlate()
        );
    }

    public VehicleResponse getVehicle(Long vehicleId) {
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

        return new VehicleResponse(
                vehicle.getUser().getId(),
                vehicle.getModel(),
                vehicle.getLicensePlate(),
                orderIds,
                appointmentIds
        );
    }
}
