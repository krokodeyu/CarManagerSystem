package database.cms.controller;

import database.cms.DTO.request.AddVehicleRequest;
import database.cms.DTO.request.ChangeVehicleRequest;
import database.cms.DTO.response.MessageResponse;
import database.cms.DTO.response.VehicleInfoResponse;
import database.cms.service.VehicleService;
import database.cms.DTO.response.VehicleChangeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<VehicleChangeResponse> addVehicle(@RequestBody AddVehicleRequest request,
                                                            Authentication authentication) {
        VehicleChangeResponse response =  vehicleService.addVehicle(request, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleInfoResponse> getVehicle(
            @PathVariable Long vehicleId
    ) {
            VehicleInfoResponse response = vehicleService.getVehicle(vehicleId);
            return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<VehicleInfoResponse>> getAllVehicles() {
        List<VehicleInfoResponse> responses = vehicleService.getAllVehicles();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<VehicleChangeResponse> updateVehicle(
            @RequestBody ChangeVehicleRequest request,
            Authentication authentication
    ) {
        VehicleChangeResponse response = vehicleService.update(request, authentication);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<MessageResponse> deleteVehicle(@PathVariable Long vehicleId){
        MessageResponse response = vehicleService.delete(vehicleId);
        return ResponseEntity.ok(response);
    }
}
