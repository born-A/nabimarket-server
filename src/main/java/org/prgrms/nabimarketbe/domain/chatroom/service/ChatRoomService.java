package org.prgrms.nabimarketbe.domain.chatroom.service;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.chatroom.entity.ChatRoom;
import org.prgrms.nabimarketbe.domain.chatroom.repository.ChatRoomRepository;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private static final String FIRESTORE_CHATROOM_COLLECTION_NAME = "chats";

    private static final String FIRESTORE_MESSAGE_COLLECTION_NAME = "messages";

    private static final HashMap<String, Object> INITIAL_MESSAGE = new HashMap<>() {
        {
            put("message", "채팅방이 개설되었습니다.");
            put("sender", "SERVER");
        }
    };

    private final ChatRoomRepository chatRoomRepository;

    private final Firestore firestore;

    @Transactional
    public void createChatRoom(Suggestion suggestion) {
        String fireStoreDocumentName = String.format(   // 예시(fromCardId=1, toCardId=2): "FROM00000001TO00000002"
                "FROM%08dTO%08d",
                suggestion.getFromCard().getCardId(),
                suggestion.getToCard().getCardId()
        );

        String fireStoreChatRoomPath = String.format(
                "/%s/%s/%s",
                FIRESTORE_CHATROOM_COLLECTION_NAME,
                fireStoreDocumentName,
                FIRESTORE_MESSAGE_COLLECTION_NAME
        );

        ChatRoom chatRoom = new ChatRoom(fireStoreChatRoomPath, suggestion);

        chatRoomRepository.save(chatRoom);

        createDocumentInFireStore(fireStoreDocumentName);
    }

    private void createDocumentInFireStore(String fireStoreDocumentName) {
        try {
            firestore.collection(FIRESTORE_CHATROOM_COLLECTION_NAME)
                    .document(fireStoreDocumentName)
                    .collection(FIRESTORE_MESSAGE_COLLECTION_NAME)
                    .document()
                    .set(INITIAL_MESSAGE);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }
    }
}
