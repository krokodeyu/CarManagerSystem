package database.cms.DTO.request;

import database.cms.entity.SparePart;

public record PartUpdateRequest(
        Long partId,
        SparePart updatedSparePart
) {
}
