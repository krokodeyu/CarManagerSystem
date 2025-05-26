package database.cms.controller;

import database.cms.service.OrderService;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
}
