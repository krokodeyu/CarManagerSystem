package database.cms.DTO.request;

public record LoginRequest(
        String username,
        String password
) {
}
