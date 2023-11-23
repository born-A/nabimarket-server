package org.prgrms.nabimarketbe.domain.chatroom.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.global.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "chat_rooms")
@NoArgsConstructor
@Getter
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @NotBlank
    @Column(nullable = false, name = "firestore_chat_room_path")
    private String fireStoreChatRoomPath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "suggestion_id")
    private Suggestion suggestion;

    public ChatRoom(
            String fireStoreChatRoomPath,
            Suggestion suggestion
    ) {
        this.fireStoreChatRoomPath = fireStoreChatRoomPath;
        this.suggestion = suggestion;
    }
}
