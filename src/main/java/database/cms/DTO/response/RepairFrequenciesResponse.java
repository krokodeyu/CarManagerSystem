package database.cms.DTO.response;

import java.util.List;

public record RepairFrequenciesResponse(
        List<jakarta.persistence.Tuple> frequencies
) {
}
