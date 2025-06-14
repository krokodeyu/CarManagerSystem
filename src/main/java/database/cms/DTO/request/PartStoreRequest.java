package database.cms.DTO.request;

import database.cms.entity.SparePart;

public record PartStoreRequest(
        String name,
        Integer quantity,
        Double price

) {
}
