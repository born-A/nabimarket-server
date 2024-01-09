package org.prgrms.nabimarketbe.jpa.chatroom.projection.list;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomListWrapper {
    private List<ChatRoomInfoListDTO> chatRoomList;

    private String nextCursorId;
}
