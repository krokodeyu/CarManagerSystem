package database.cms.DTO.response;

import java.time.LocalDateTime;

public record UserInfoResponse(
        Long id,
        String name,
        String email,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
