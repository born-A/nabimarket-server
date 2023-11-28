package org.prgrms.nabimarketbe.domain.chatroom.repository;

import org.prgrms.nabimarketbe.domain.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    boolean existsChatRoomByFireStoreChatRoomPath(String firestoreChatRoomPath);
}
