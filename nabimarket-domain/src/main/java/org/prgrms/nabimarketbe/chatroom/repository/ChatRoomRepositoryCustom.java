package org.prgrms.nabimarketbe.chatroom.repository;

import org.prgrms.nabimarketbe.chatroom.projection.list.ChatRoomListWrapper;
import org.prgrms.nabimarketbe.chatroom.projection.single.ChatRoomInfoWrapper;

public interface ChatRoomRepositoryCustom {
    ChatRoomInfoWrapper getChatRoomInfoById(Long chatRoomId, Long completeRequestId);

    ChatRoomListWrapper getChatRoomList(Integer size, String cursorId, Long userId);
}
