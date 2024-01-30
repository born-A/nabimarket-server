package org.prgrms.nabimarketbe.chatroom.service;

import com.google.cloud.firestore.CollectionReference;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.category.entity.Category;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.chatroom.entity.ChatRoom;
import org.prgrms.nabimarketbe.chatroom.repository.ChatRoomRepository;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {
    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private Firestore firestore;

    private static Long fromCardId;
    private static Long toCardId;
    private static Card fromCard;
    private static Card toCard;

    class ChatRoomMatcher implements ArgumentMatcher<ChatRoom> {
        private String fireStoreChatRoomPath;

        private Suggestion suggestion;

        public ChatRoomMatcher(String fireStoreChatRoomPath, Suggestion suggestion) {
            this.fireStoreChatRoomPath = fireStoreChatRoomPath;
            this.suggestion = suggestion;
        }

        @Override
        public boolean matches(ChatRoom chatRoom) {
            return chatRoom.getFireStoreChatRoomPath().equals(fireStoreChatRoomPath)
                && chatRoom.getSuggestion().equals(suggestion);
        }
    }

    @BeforeEach
    void setUp() {
        Category category = new Category(CategoryEnum.ELECTRONICS);
        Item item = Item.builder()
            .priceRange(PriceRange.PRICE_RANGE_FOUR)
            .itemName("apple macbook pro")
            .category(category)
            .build();

        // fromCard setting
        User fromCardOwner = User.builder()
            .role("USER")
            .provider("GOOGLE")
            .nickname("doby")
            .imageUrl("img_url")
            .accountId("1010110")
            .build();

        fromCardId = 1L;
        fromCard = Card.builder()
            .cardTitle("macbook pro 교환 원해요")
            .content("상태 최상입니다.")
            .tradeType(TradeType.DIRECT_DEALING)
            .pokeAvailable(true)
            .tradeArea("서울")
            .thumbnail("thumbnail_img_url")
            .item(item)
            .user(fromCardOwner)
            .build();
        ReflectionTestUtils.setField(fromCard, "cardId", fromCardId);
        ReflectionTestUtils.setField(fromCard, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(fromCard, "modifiedDate", LocalDateTime.now());

        // toCard setting
        User toCardOwner = User.builder()
            .role("USER")
            .provider("GOOGLE")
            .nickname("master")
            .imageUrl("img_url")
            .accountId("1010111")
            .build();

        toCardId = 2L;
        toCard = Card.builder()
            .cardTitle("galaxy pro 교환 원해요")
            .content("상태 최상입니다.")
            .tradeType(TradeType.DIRECT_DEALING)
            .pokeAvailable(true)
            .tradeArea("서울")
            .thumbnail("thumbnail_img_url")
            .item(item)
            .user(toCardOwner)
            .build();
        ReflectionTestUtils.setField(toCard, "cardId", toCardId);
    }

    @Test
    @DisplayName("거래 수락 시에 채팅방이 개설된다.")
    void createChatRoom() {
        // given
        Suggestion suggestion = Suggestion.builder()
            .suggestionType(SuggestionType.OFFER)
            .fromCard(fromCard)
            .toCard(toCard)
            .build();

        suggestion.decideSuggestion(true);

        String expectFireStoreChatRoomPath = String.format("/chats/FROM%08dTO%08d/messages", fromCardId, toCardId);

        when(chatRoomRepository.existsChatRoomBySuggestion(any())).thenReturn(false);

        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocument = mock(DocumentReference.class);
        when(firestore.collection(any())).thenReturn(mockCollection);
        when(firestore.collection(any()).document(any())).thenReturn(mockDocument);
        when(firestore.collection(any()).document(any()).collection(any())).thenReturn(mockCollection);
        when(firestore.collection(any()).document(any()).collection(any()).document()).thenReturn(mockDocument);

        // when
        chatRoomService.createChatRoom(suggestion);

        // then
        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatRoomRepository).save(argThat(new ChatRoomMatcher(expectFireStoreChatRoomPath, suggestion)));
    }
}