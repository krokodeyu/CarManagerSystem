package database.cms.DTO.request;

import database.cms.entity.Notification;

public record NotificationReturnRequest(
        Long notificationId,
        Boolean accept
) {
}
