package org.prgrms.nabimarketbe.domain.chatroom.api;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.service.ChatRoomService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class CharRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<SingleResult<ChatRoomInfoWrapper>> getChatRoomById(
        @RequestHeader(name = "Authorization") String token,
        @PathVariable Long chatRoomId
    ) {
        ChatRoomInfoWrapper charRoomInfo = chatRoomService.getChatRoomById(token, chatRoomId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(charRoomInfo));
    }
}
