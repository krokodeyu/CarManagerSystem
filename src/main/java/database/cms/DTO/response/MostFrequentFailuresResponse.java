package database.cms.DTO.response;

import java.util.List;

public record MostFrequentFailuresResponse(
        String failures,
        Long times
) {
}
