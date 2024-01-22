package org.prgrms.nabimarketbe.notification.projection;

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
