package database.cms.DTO.response;

import java.util.List;

public record NegativeCommentOrdersResponse(
        List<jakarta.persistence.Tuple> result // orders technicians ratings

) {
}
