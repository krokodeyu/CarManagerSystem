package database.cms.DTO.response;

import database.cms.entity.User;

public record UserCheckResponse (
        User user
) {
}
