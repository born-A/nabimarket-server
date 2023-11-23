package org.prgrms.nabimarketbe.domain.chatroom.dto.response.single;

import lombok.Getter;

@Getter
public class ChatRoomInfoDTO {
    String fireStoreChatRoomId;
    FromCardInfoDTO fromCardInfo;
    ToCardInfoDTO toCardInfo;
    Long completeRequestId;

    public void setCompleteRequestId(Long completeRequestId) {
        this.completeRequestId = completeRequestId;
    }

    public void updateFireStoreChatRoomId() {
        String fireStoreDocumentName = fireStoreChatRoomId.split("/")[2];

        this.fireStoreChatRoomId = fireStoreDocumentName;
    }
}
