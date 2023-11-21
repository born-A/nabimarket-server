package org.prgrms.nabimarketbe.domain.notifiaction.service;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.NotificationResponseDTO;
import org.prgrms.nabimarketbe.domain.notifiaction.entity.Notification;
import org.prgrms.nabimarketbe.domain.notifiaction.repository.NotificationRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationResponseDTO createNotification(
        User receiver,
        Card card,
        String content
    ) {
        Notification notification = new Notification(
            receiver,
            card,
            content
        );

        Notification savedNotification = notificationRepository.save(notification);

        return NotificationResponseDTO.from(savedNotification);
    }
}
