package database.cms.DTO.response;

import database.cms.entity.TechSpec;
import database.cms.entity.Technician;

import java.time.LocalDateTime;
import java.util.List;

public record TechnicianCheckResponse (
        Long id,
        String name,
        String phone,
        TechSpec specialization,
        LocalDateTime createdAt,
        List<Long> appointmentId
){
}
