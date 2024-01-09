package org.prgrms.nabimarketbe.jpa.notification.projection;

import java.util.List;

import org.prgrms.nabimarketbe.jpa.notification.projection.NotificationDetailResponseDTO;

public record NotificationPagingResponseDTO(
    List<NotificationDetailResponseDTO> notificationList,
    String nextCursorId
) {
}
