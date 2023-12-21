package org.prgrms.nabimarketbe.global.event;

import org.prgrms.nabimarketbe.notifiaction.service.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {
    private final NotificationService notificationService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
        classes = NotificationCreateEvent.class,
        phase = TransactionPhase.AFTER_COMMIT
    )
    public void createNotification(NotificationCreateEvent notificationCreateEvent) {
        notificationService.createNotification(
            notificationCreateEvent.getReceiver(),
            notificationCreateEvent.getCard(),
            notificationCreateEvent.getContent()
        );
    }
}
