package database.cms.DTO.response;

import database.cms.entity.Appointment;
import database.cms.entity.Technician;

import java.util.List;

public record NegativeCommentOrdersResponse(
        List<Object[]> result // orders technicians ratings

) {
}
