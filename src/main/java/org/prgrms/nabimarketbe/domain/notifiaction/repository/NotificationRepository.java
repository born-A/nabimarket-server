package org.prgrms.nabimarketbe.domain.notifiaction.repository;

import org.prgrms.nabimarketbe.domain.notifiaction.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
