package org.prgrms.nabimarketbe.domain.chatroom.dto.response.list;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomInfoListDTO {
    Long chatRoomId;
    LocalDateTime createdAt;
    FromCardInfoListDTO fromCardInfo;
    ToCardInfoListDTO toCardInfo;
}
