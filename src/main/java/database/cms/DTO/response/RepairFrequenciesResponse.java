package database.cms.DTO.response;

import java.util.List;

public record RepairFrequenciesResponse(
        List<Object[]> frequencies
) {
}
