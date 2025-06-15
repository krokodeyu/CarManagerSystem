package database.cms.DTO.response;

import java.util.List;

public record TechnicianPerformanceResponse(
        List<jakarta.persistence.Tuple> performance
) {
}
