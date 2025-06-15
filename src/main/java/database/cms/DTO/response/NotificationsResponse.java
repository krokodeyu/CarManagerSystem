package database.cms.DTO.response;

import java.util.List;

public record NotificationsResponse(
        List<NotificationResponse> notifications,
        boolean hasNew
) {
}
