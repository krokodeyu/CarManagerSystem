package database.cms.controller;

import database.cms.DTO.request.AddVehicleRequest;
import database.cms.DTO.response.VehicleResponse;
import database.cms.service.VehicleService;
import database.cms.DTO.response.AddVehicleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AddVehicleResponse> addVehicle(@RequestBody AddVehicleRequest request, Authentication authentication) {
        AddVehicleResponse response =  vehicleService.addVehicle(request, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<VehicleResponse> getVehicle(
            @RequestParam Long vehicleId
    ) {
            VehicleResponse response = vehicleService.getVehicle(vehicleId);
            return ResponseEntity.ok(response);
    }
}
