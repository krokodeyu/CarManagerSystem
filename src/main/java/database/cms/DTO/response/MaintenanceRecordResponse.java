package database.cms.DTO.response;

import database.cms.entity.Order;

import java.util.List;

public record MaintenanceRecordResponse (
        List<Order> orders
){
}
