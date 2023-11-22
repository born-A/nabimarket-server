package org.prgrms.nabimarketbe.domain.notifiaction.repository;

import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.wrapper.NotificationPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
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
