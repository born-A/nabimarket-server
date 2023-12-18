package org.prgrms.nabimarketbe.domain.chatroom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.ChatRoomListWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.service.ChatRoomService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅방", description = "채팅방 생성 및 접근 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class CharRoomController {
    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 단건 조회", description = "채팅방 단건 조회를 할 수 있습니다.")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<SingleResult<ChatRoomInfoWrapper>> getChatRoomById(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "채팅방 접근을 위한 id(pk)", required = true)
        @PathVariable Long chatRoomId
    ) {
        ChatRoomInfoWrapper charRoomInfo = chatRoomService.getChatRoomById(token, chatRoomId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(charRoomInfo));
    }

    @Operation(summary = "채팅방 목록 조회", description = "개설된 채팅방 목록 조회를 할 수 있습니다.")
    @GetMapping
    public ResponseEntity<SingleResult<ChatRoomListWrapper>> getChatRooms(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "채팅방 목록 페이징을 위한 cursor id")
        @RequestParam(required = false) String cursorId,
        @Parameter(description = "채팅방 목록 페이지 크기", required = true)
        @RequestParam Integer size
    ) {
        ChatRoomListWrapper chatRoomList = chatRoomService.getChatRooms(token, cursorId, size);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(chatRoomList));
    }
}
