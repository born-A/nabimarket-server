package org.prgrms.nabimarketbe.jpa.chatroom.repository;

import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.ChatRoomListWrapper;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.ChatRoomInfoWrapper;

public interface ChatRoomRepositoryCustom {
    ChatRoomInfoWrapper getChatRoomInfoById(Long chatRoomId, Long completeRequestId);

    ChatRoomListWrapper getChatRoomList(Integer size, String cursorId, Long userId);
}
