package database.cms.DTO.response;

import database.cms.entity.SparePart;

import java.time.LocalDateTime;
import java.util.List;

public record PartsResponse(
        Long id,
        String name,
        Double price,
        Integer quantity,
        LocalDateTime createdAt

) {
}
