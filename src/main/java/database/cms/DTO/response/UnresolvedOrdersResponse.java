package database.cms.DTO.response;

import java.util.List;

public record UnresolvedOrdersResponse(
        List<Object[]> unresolvedOrders
) {
}
