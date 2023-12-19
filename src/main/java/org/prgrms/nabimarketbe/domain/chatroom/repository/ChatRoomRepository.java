package org.prgrms.nabimarketbe.domain.chatroom.repository;

import org.prgrms.nabimarketbe.domain.chatroom.entity.ChatRoom;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    boolean existsChatRoomBySuggestion(Suggestion suggestion);
}
