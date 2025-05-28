package database.cms.DTO.request;

public record UserUpdateRequest(
        Long id,
        String name,
        String email,
        String password
) {
}
