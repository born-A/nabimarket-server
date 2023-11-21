package org.prgrms.nabimarketbe.domain.notifiaction.repository;

import org.prgrms.nabimarketbe.domain.notifiaction.entity.Notification;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    Long countNotificationByIsReadIsFalseAndReceiver(User receiver);
}
