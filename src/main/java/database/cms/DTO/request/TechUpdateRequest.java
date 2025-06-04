package database.cms.DTO.request;

import database.cms.entity.TechSpec;

public record TechUpdateRequest(
        String phone,
        TechSpec techSpec,
        String password
) {
}
