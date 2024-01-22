package org.prgrms.nabimarketbe.chatroom.projection.single;

import lombok.Getter;

@Getter
public class ChatRoomInfoDTO {
    private String fireStoreChatRoomId;

    private FromCardInfoDTO fromCardInfo;

    private ToCardInfoDTO toCardInfo;

    private Long completeRequestId;

    public void setCompleteRequestId(Long completeRequestId) {
        this.completeRequestId = completeRequestId;
    }

    public void updateFireStoreChatRoomId() {
        String fireStoreDocumentName = fireStoreChatRoomId.split("/")[2];

        this.fireStoreChatRoomId = fireStoreDocumentName;
    }
}
