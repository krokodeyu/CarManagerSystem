package database.cms.DTO.response;

import database.cms.entity.Notification;

import java.util.List;
import java.util.Optional;

public record NotificationResponse(
        Long id,
        Optional<Long> userId,
        Optional<Long> techId,
        String content
) {
}
