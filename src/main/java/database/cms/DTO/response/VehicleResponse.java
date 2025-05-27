package database.cms.DTO.response;

import java.util.List;

public record VehicleResponse (
        Long userId,
        String model,
        String licensePlate,
        List<Long> orderIds,
        List<Long> appointmentIds
) {
}
