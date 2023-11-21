package org.prgrms.nabimarketbe.domain.notifiaction.dto.response.wrapper;

import java.util.List;

public record NotificationListResponseDTO<T>(List<T> notificationList) {
}
