package database.cms.controller;

import database.cms.service.VehicleService;
import org.springframework.stereotype.Controller;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
}
