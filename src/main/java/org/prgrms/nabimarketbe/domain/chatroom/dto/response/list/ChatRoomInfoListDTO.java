package org.prgrms.nabimarketbe.domain.chatroom.dto.response.list;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomInfoListDTO {
    private Long chatRoomId;
    private LocalDateTime createdAt;
    private FromCardInfoListDTO fromCardInfo;
    private ToCardInfoListDTO toCardInfo;
}
