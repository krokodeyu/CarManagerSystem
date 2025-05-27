package database.cms.DTO.request;

public record UserRegisterRequest(
        String name,
        String password,
        String email,
        String phone
) {
}
