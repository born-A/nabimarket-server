package org.prgrms.nabimarketbe.domain.chatroom.repository;

import org.prgrms.nabimarketbe.domain.chatroom.dto.response.ChatRoomInfoWrapper;

public interface ChatRoomRepositoryCustom {
    ChatRoomInfoWrapper getChatRoomInfoById(Long chatRoomId, Long completeRequestId);
}
