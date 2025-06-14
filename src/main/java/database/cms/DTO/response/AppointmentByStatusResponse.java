package database.cms.DTO.response;

import java.util.List;

public record AppointmentByStatusResponse(
        List<Long> appointmentIds
) {
}
