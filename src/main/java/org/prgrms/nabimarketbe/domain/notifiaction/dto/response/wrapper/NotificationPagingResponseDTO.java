package org.prgrms.nabimarketbe.domain.notifiaction.dto.response.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.projection.NotificationDetailResponseDTO;

public record NotificationPagingResponseDTO(
    List<NotificationDetailResponseDTO> notificationList,
    String nextCursorId
) {
}
