package org.prgrms.nabimarketbe.chatroom;


import lombok.Getter;
import org.prgrms.nabimarketbe.suggestion.Suggestion;

@Getter
public class ChatRoom {
    private Long chatRoomId;
    private String fireStoreChatRoomPath;
    private Suggestion suggestion;

    public ChatRoom(
        String fireStoreChatRoomPath,
        Suggestion suggestion
    ) {
        this.fireStoreChatRoomPath = fireStoreChatRoomPath;
        this.suggestion = suggestion;
    }
}
