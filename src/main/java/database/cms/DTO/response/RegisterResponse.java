package database.cms.DTO.response;

import java.time.LocalDateTime;

public record RegisterResponse (
        Long id,
        String name,
        String email,
        LocalDateTime createdAt
){
}
