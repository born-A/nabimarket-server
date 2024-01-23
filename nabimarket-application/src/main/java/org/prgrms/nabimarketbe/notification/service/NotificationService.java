package org.prgrms.nabimarketbe.notification.service;

import java.util.List;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;

import org.prgrms.nabimarketbe.notification.dto.request.NotificationReadRequestDTO;
import org.prgrms.nabimarketbe.notification.dto.response.NotificationUnreadCountResponseDTO;
import org.prgrms.nabimarketbe.notification.entity.Notification;
import org.prgrms.nabimarketbe.notification.repository.NotificationRepository;
import org.prgrms.nabimarketbe.notification.wrapper.NotificationPagingResponseDTO;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserRepository;
import org.prgrms.nabimarketbe.user.service.CheckService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public void createNotification(
        User receiver,
        Card card,
        String content
    ) {
        Notification notification = new Notification(
            receiver,
            card,
            content
        );

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public NotificationUnreadCountResponseDTO getUnreadNotificationCount(String token) {
        Long receiverId = checkService.parseToken(token);
        User receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Long unReadCount = notificationRepository.countNotificationByIsReadIsFalseAndReceiver(receiver);
        NotificationUnreadCountResponseDTO responseDTO = new NotificationUnreadCountResponseDTO(unReadCount);

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public NotificationPagingResponseDTO getNotificationsByIsRead(
        String token,
        Boolean isRead,
        Integer size,
        String cursorId
    ) {
        Long userId = checkService.parseToken(token);
        User receiver = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Sort sort = Sort.by(
            Sort.Order.desc("createdDate"),
            Sort.Order.desc("notificationId")
        );

        PageRequest pageRequest = PageRequest.of(
            0,
            size,
            sort
        );

        NotificationPagingResponseDTO notificationPagingResponseDTO = notificationRepository.getNotificationsByIsRead(
            receiver,
            isRead,
            size,
            cursorId,
            pageRequest
        );

        return notificationPagingResponseDTO;
    }

    @Transactional
    public void updateNotificationToRead(
        String token,
        NotificationReadRequestDTO notificationReadRequestDTO
    ) {
        Long userId = checkService.parseToken(token);
        User receiver = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        List<Long> notificationIds = notificationReadRequestDTO.notificationIds();

        notificationRepository.bulkUpdateNotificationsByIds(notificationIds, receiver);
    }
}
