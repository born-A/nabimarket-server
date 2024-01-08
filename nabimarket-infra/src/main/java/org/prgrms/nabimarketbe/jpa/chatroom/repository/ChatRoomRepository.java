package org.prgrms.nabimarketbe.jpa.chatroom.repository;

import org.prgrms.nabimarketbe.jpa.chatroom.entity.ChatRoom;
import org.prgrms.nabimarketbe.jpa.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    boolean existsChatRoomBySuggestion(Suggestion suggestion);
}
