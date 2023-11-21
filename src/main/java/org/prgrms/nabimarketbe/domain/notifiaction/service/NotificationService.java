package org.prgrms.nabimarketbe.domain.notifiaction.service;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.NotificationDetailResponseDTO;
import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.NotificationUnreadCountResponseDTO;
import org.prgrms.nabimarketbe.domain.notifiaction.entity.Notification;
import org.prgrms.nabimarketbe.domain.notifiaction.repository.NotificationRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final CheckService checkService;

    @Transactional
    public NotificationDetailResponseDTO createNotification(
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

        return NotificationDetailResponseDTO.from(savedNotification);
    }

    @Transactional(readOnly = true)
    public NotificationUnreadCountResponseDTO getUnreadNotificationCount(String token) {
        Long recieverId = checkService.parseToken(token);
        User receiver = userRepository.findById(recieverId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Long unReadCount = notificationRepository.countNotificationsBySeenIsFalseAndReceiver(receiver);
        NotificationUnreadCountResponseDTO responseDTO = new NotificationUnreadCountResponseDTO(unReadCount);

        return responseDTO;
    }
}
