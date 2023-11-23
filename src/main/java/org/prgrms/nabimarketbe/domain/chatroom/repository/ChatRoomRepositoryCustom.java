package org.prgrms.nabimarketbe.domain.chatroom.repository;

import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.ChatRoomListWrapper;

public interface ChatRoomRepositoryCustom {
    ChatRoomInfoWrapper getChatRoomInfoById(Long chatRoomId, Long completeRequestId);
    ChatRoomListWrapper getChatRoomList(Integer size, String cursorId, Long userId);
}
