package database.cms.controller;

import database.cms.service.StatService;
import org.springframework.stereotype.Controller;

@Controller
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }
}
