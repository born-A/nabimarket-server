package org.prgrms.nabimarketbe.domain.chatroom.dto.response.list;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ChatRoomListWrapper {
    private List<ChatRoomInfoListDTO> chatRoomList;

    private String nextCursorId;
}
