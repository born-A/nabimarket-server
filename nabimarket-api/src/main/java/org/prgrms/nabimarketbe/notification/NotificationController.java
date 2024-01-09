package org.prgrms.nabimarketbe.notification;

import javax.validation.Valid;

import org.prgrms.nabimarketbe.model.CommonResult;
import org.prgrms.nabimarketbe.model.ResponseFactory;
import org.prgrms.nabimarketbe.model.SingleResult;
import org.prgrms.nabimarketbe.notification.dto.request.NotificationReadRequestDTO;
import org.prgrms.nabimarketbe.notification.dto.response.NotificationUnreadCountResponseDTO;
import org.prgrms.nabimarketbe.notification.service.NotificationService;
import org.prgrms.nabimarketbe.jpa.notification.projection.NotificationPagingResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "알림", description = "알림 기능 관련 API 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "안 읽은 알림 수 조회", description = "읽지 않은 알림의 개수를 조회할 수 있습니다.")
    @GetMapping("/unread-count")
    public ResponseEntity<SingleResult<NotificationUnreadCountResponseDTO>> getUnreadNotificationCount(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token
    ) {
        NotificationUnreadCountResponseDTO responseDTO = notificationService.getUnreadNotificationCount(token);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(responseDTO));
    }

    @Operation(summary = "알림 목록 조회", description = "읽음 여부에 따른 알림의 목록을 조회할 수 있습니다.")
    @GetMapping
    public ResponseEntity<SingleResult<NotificationPagingResponseDTO>> getNotificationsByIsRead(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "알림에 대한 읽음 여부(T/F)", required = true)
        @RequestParam("is-read") Boolean isRead,
        @Parameter(description = "page 크기", required = true)
        @RequestParam Integer size,
        @Parameter(description = "커서 id")
        @RequestParam(required = false) String cursorId
    ) {
        NotificationPagingResponseDTO responseDTO = notificationService.getNotificationsByIsRead(
            token,
            isRead,
            size,
            cursorId
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(responseDTO));
    }

    @Operation(summary = "알림 읽음 처리", description = "알림에 대해 읽음 처리할 수 있습니다.")
    @PutMapping("/read")
    public ResponseEntity<CommonResult> updateNotificationToRead(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "읽음 처리할 알림 id 목록", required = true)
        @RequestBody @Valid NotificationReadRequestDTO notificationReadRequestDTO
    ) {
        notificationService.updateNotificationToRead(token, notificationReadRequestDTO);

        return ResponseEntity.ok(ResponseFactory.getSuccessResult());
    }
}
