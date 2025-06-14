package database.cms.DTO.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record VehicleInfoResponse(
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Long userId,
        String model,
        String licensePlate,
        List<Long> appointmentIds
) {
}
