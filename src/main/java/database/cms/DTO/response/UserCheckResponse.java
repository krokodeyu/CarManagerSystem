package database.cms.DTO.response;

import database.cms.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public record UserCheckResponse (
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        List<Long> vehicleIds,
        List<Long> appointmentIds,
        List<Long> feedbackIds
) {
}
