package database.cms.controller;

import database.cms.service.TechnicianService;
import org.springframework.stereotype.Controller;

@Controller
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }
}
