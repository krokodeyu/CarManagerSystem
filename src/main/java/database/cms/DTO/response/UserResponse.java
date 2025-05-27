package database.cms.DTO.response;

import jakarta.validation.constraints.NotNull;

public record UserResponse (
        Long id,
        String name,
        String email,
        String phone,
        @NotNull
        String address
){
}
