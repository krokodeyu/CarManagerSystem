package database.cms.DTO.request;

import database.cms.entity.TechSpec;

public record TechRegisterRequest(
        String name,
        String phone,
        TechSpec techSpec,
        String password
) {
}
