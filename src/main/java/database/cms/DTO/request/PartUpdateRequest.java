package database.cms.DTO.request;

import database.cms.entity.SparePart;

import java.util.Optional;

public record PartUpdateRequest(
        Long partId,
        Optional<String> name,
        Optional<Double> price,
        Optional<Integer> quantity

) {
}
