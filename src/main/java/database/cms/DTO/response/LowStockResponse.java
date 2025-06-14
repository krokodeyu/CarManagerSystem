package database.cms.DTO.response;

import database.cms.entity.SparePart;

import java.util.List;

public record LowStockResponse(
        List<SparePart> lowStock
) {
}
