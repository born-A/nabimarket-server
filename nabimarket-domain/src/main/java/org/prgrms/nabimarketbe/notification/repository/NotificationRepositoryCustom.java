package org.prgrms.nabimarketbe.notification.repository;

import org.prgrms.nabimarketbe.notification.wrapper.NotificationPagingResponseDTO;
import org.prgrms.nabimarketbe.user.entity.User;
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
