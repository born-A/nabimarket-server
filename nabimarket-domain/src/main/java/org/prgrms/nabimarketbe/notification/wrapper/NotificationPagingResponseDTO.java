package org.prgrms.nabimarketbe.notification.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.notification.projection.NotificationDetailResponseDTO;

public record NotificationPagingResponseDTO(
    List<NotificationDetailResponseDTO> notificationList,
    String nextCursorId
) {
}
