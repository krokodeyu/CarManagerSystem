package database.cms.DTO.response;

import database.cms.entity.Notification;

import java.util.List;

public record NotificationResponse(
        List<Notification> notificationList
) {
}
