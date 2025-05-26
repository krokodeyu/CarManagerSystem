package database.cms.controller;

import database.cms.service.PartService;
import org.springframework.stereotype.Controller;

@Controller
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }
}
