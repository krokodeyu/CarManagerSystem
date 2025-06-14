package database.cms.DTO.response;

import java.util.List;

public record FeeProportionResponse(
        List<Object[]> feeProportions
) {
}
