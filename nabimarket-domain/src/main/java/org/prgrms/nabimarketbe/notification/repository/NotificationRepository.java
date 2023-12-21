package org.prgrms.nabimarketbe.notification.repository;

import java.util.List;

import org.prgrms.nabimarketbe.notification.entity.Notification;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    Long countNotificationByIsReadIsFalseAndReceiver(User receiver);

    @Query("UPDATE Notification n "
        + "SET n.isRead=true "
        + "WHERE n.receiver = :receiver AND n.notificationId in :notificationIds")
    @Modifying(clearAutomatically = true)
    void bulkUpdateNotificationsByIds(List<Long> notificationIds, User receiver);
}
