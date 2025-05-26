package database.cms.controller;

import database.cms.service.AppointmentService;
import org.springframework.stereotype.Controller;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

}
