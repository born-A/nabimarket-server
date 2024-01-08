package org.prgrms.nabimarketbe.jpa.notification.repository;

import org.prgrms.nabimarketbe.jpa.notification.projection.NotificationPagingResponseDTO;
import org.prgrms.nabimarketbe.jpa.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryCustom {
    NotificationPagingResponseDTO getNotificationsByIsRead(
        User receiver,
        Boolean isRead,
        Integer size,
        String cursorId,
        Pageable pageable
    );
}
