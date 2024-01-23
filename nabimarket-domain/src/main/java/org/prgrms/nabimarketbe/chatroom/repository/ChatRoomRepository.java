package org.prgrms.nabimarketbe.chatroom.repository;

import org.prgrms.nabimarketbe.chatroom.entity.ChatRoom;
import org.prgrms.nabimarketbe.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    boolean existsChatRoomBySuggestion(Suggestion suggestion);
}
