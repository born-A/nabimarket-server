package org.prgrms.nabimarketbe.domain.chatroom.service;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.entity.ChatRoom;
import org.prgrms.nabimarketbe.domain.chatroom.repository.ChatRoomRepository;
import org.prgrms.nabimarketbe.domain.completeRequest.repository.CompleteRequestRepository;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private static final String FIRESTORE_CHATROOM_COLLECTION_NAME = "chats";

    private static final String FIRESTORE_MESSAGE_COLLECTION_NAME = "messages";

    private final CheckService checkService;

    private final ChatRoomRepository chatRoomRepository;

    private final CompleteRequestRepository completeRequestRepository;

    private final Firestore firestore;

    @Transactional
    public void createChatRoom(Suggestion suggestion) {
        String fireStoreDocumentName = String.format(
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

    @Transactional(readOnly = true)
    public ChatRoomInfoWrapper getChatRoomById(
            String token,
            Long chatRoomId
    ) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        Card fromCard = chatRoom
                .getSuggestion()
                .getFromCard();

        Card toCard = chatRoom
                .getSuggestion()
                .getToCard();

        checkAuthorityForCards(token, fromCard, toCard);

        Long completeRequestId = getCompleteRequestId(fromCard, toCard);

        return chatRoomRepository.getChatRoomInfoById(chatRoomId, completeRequestId);
    }

    private void createDocumentInFireStore(String fireStoreDocumentName) {
        try {
            Map<String, Object> initialMessage = generateInitialMessage();

            firestore.collection(FIRESTORE_CHATROOM_COLLECTION_NAME)
                    .document(fireStoreDocumentName)
                    .collection(FIRESTORE_MESSAGE_COLLECTION_NAME)
                    .document()
                    .set(initialMessage);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }
    }

    private Map<String, Object> generateInitialMessage() {
        return new HashMap<>() {
            {
                put("text", "채팅방이 개설되었습니다.");
                put("sender", "SERVER");
                put("createdAt", FieldValue.serverTimestamp());
            }
        };
    }

    private void checkAuthorityForCards(String token, Card fromCard, Card toCard) {
        if (!checkService.isEqual(token, fromCard.getUser().getUserId())
                && !checkService.isEqual(token, toCard.getUser().getUserId())
        ) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }
    }

    private Long getCompleteRequestId(Card fromCard, Card toCard) {
        if (completeRequestRepository.existsByFromCardAndToCard(fromCard, toCard)) {
            return completeRequestRepository.findCompleteRequestByFromCardAndToCard(fromCard, toCard)
                    .get()
                    .getCompleteRequestId();
        } else if (completeRequestRepository.existsByFromCardAndToCard(toCard, fromCard)) {
            return completeRequestRepository.findCompleteRequestByFromCardAndToCard(toCard, fromCard)
                    .get()
                    .getCompleteRequestId();
        } else {
            return Long.valueOf(-1);
        }
    }
}
