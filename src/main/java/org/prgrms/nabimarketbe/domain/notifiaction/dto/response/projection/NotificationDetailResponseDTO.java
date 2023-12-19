package org.prgrms.nabimarketbe.domain.notifiaction.dto.response.projection;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class NotificationDetailResponseDTO {
    private Long notificationId;

    private String content;

    private boolean isRead;

    private Long cardId;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
