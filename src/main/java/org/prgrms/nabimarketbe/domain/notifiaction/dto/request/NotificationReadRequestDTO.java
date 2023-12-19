package org.prgrms.nabimarketbe.domain.notifiaction.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

public record NotificationReadRequestDTO(
    @NotNull(message = "NotificationIds가 null이면 안됩니다.")
    List<Long> notificationIds
) {
}
