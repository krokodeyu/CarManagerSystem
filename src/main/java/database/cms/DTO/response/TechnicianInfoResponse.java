package database.cms.DTO.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public record TechnicianInfoResponse(
        Long id,
        String name,
        String phone,
        String specialization,
        LocalDateTime registerTime
){
}
