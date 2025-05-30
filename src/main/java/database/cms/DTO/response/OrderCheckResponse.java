package database.cms.DTO.response;

import database.cms.entity.Order;

public record OrderCheckResponse (
        Order order
){
}
