package org.prgrms.nabimarketbe.domain.notifiaction.api;

import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.wrapper.NotificationListResponseDTO;
import org.prgrms.nabimarketbe.domain.notifiaction.dto.response.NotificationUnreadCountResponseDTO;
import org.prgrms.nabimarketbe.domain.notifiaction.service.NotificationService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotifiactionController {
    private final NotificationService notificationService;

    @GetMapping("/unread-count")
    public ResponseEntity<SingleResult<NotificationUnreadCountResponseDTO>> getUnreadNotificationCount(
        @RequestHeader("Authorization") String token
    ) {
        NotificationUnreadCountResponseDTO responseDTO = notificationService.getUnreadNotificationCount(token);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(responseDTO));
    }

    @GetMapping
    public ResponseEntity<SingleResult<NotificationListResponseDTO>>
}
