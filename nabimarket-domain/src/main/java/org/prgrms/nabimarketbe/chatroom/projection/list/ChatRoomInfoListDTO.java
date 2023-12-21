package org.prgrms.nabimarketbe.chatroom.projection.list;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ChatRoomInfoListDTO {
    private Long chatRoomId;

    private LocalDateTime createdAt;

    private FromCardInfoListDTO fromCardInfo;

    private ToCardInfoListDTO toCardInfo;
}
