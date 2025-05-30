package database.cms.DTO.response;

import database.cms.entity.User;

import java.util.List;

public record AllUsersResponse(
        List<User> users
) {
}
