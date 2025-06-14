package database.cms.DTO.response;

import java.util.List;

public record ApppintmentByStatusResponse(
        List<Long> appointmentIds
) {
}
