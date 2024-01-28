package org.prgrms.nabimarketbe.suggestion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.card.repository.CardRepository;
import org.prgrms.nabimarketbe.category.entity.Category;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.chatroom.service.ChatRoomService;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.suggestion.dto.request.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.suggestion.dto.response.SuggestionResponseDTO;
import org.prgrms.nabimarketbe.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.suggestion.projection.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.suggestion.projection.SuggestionListReadResponseDTO;
import org.prgrms.nabimarketbe.suggestion.repository.SuggestionRepository;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserRepository;
import org.prgrms.nabimarketbe.user.service.CheckService;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SuggestionServiceTest {
    @InjectMocks
    private SuggestionService suggestionService;

    @Mock
    private CheckService checkService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private ChatRoomService chatRoomService;

    private String tokenA;
    private Long fromUserId;
    private Long toCardId;
    private Long fromCardId;
    private Long suggestionId;
    private SuggestionStatus suggestionStatus;
    private LocalDateTime createdDate;
    private User fromUser;
    private Card toCard;
    private Card fromCard;
    private Suggestion suggestion;

    @BeforeEach
    public void setUp() {
        tokenA = "tokenA";

        SuggestionType suggestionType = SuggestionType.OFFER;
        suggestionStatus = SuggestionStatus.WAITING;
        
        fromUserId = 1L;
        Long toUserId = 2L;

        toCardId = 1L;
        fromCardId = 2L;
        
        suggestionId = 1L;

        createdDate = LocalDateTime.now();

        fromUser = User.builder()
            .role("USER")
            .provider("GOOGLE")
            .nickname("A")
            .imageUrl("test_url")
            .accountId("test_B_Id")
            .build();

        ReflectionTestUtils.setField(fromUser, "userId", fromUserId);

        User toUser = User.builder()
            .role("USER")
            .provider("GOOGLE")
            .nickname("B")
            .imageUrl("test_url")
            .accountId("test_A_Id")
            .build();

        ReflectionTestUtils.setField(toUser, "userId", toUserId);

        Category category = new Category(CategoryEnum.ELECTRONICS);

        Item item = Item.builder()
            .priceRange(PriceRange.PRICE_RANGE_ONE)
            .itemName("test_item")
            .category(category)
            .build();

        fromCard = Card.builder()
            .cardTitle("test_title")
            .content("test")
            .tradeType(TradeType.DIRECT_DEALING)
            .pokeAvailable(true)
            .tradeArea("일산")
            .thumbnail("test_url")
            .user(fromUser)
            .item(item)
            .build();

        ReflectionTestUtils.setField(fromCard,"cardId", fromCardId);
        ReflectionTestUtils.setField(fromCard, "createdDate", createdDate);

        toCard = Card.builder()
            .cardTitle("test_title")
            .content("test")
            .tradeType(TradeType.DIRECT_DEALING)
            .pokeAvailable(true)
            .tradeArea("일산")
            .thumbnail("test_url")
            .user(toUser)
            .item(item)
            .build();

        ReflectionTestUtils.setField(toCard, "cardId", toCardId);
        ReflectionTestUtils.setField(toCard, "createdDate", createdDate);

        suggestion = Suggestion.builder()
            .suggestionType(suggestionType)
            .toCard(toCard)
            .fromCard(fromCard)
            .build();

        ReflectionTestUtils.setField(suggestion, "suggestionId", suggestionId);
        ReflectionTestUtils.setField(suggestion, "createdDate", createdDate);
    }

    @Test
    @DisplayName("사용자 A는 사용자 B에게 제안을 한다.")
    public void createSuggestion() {
        //given
        SuggestionType suggestionType = SuggestionType.OFFER;
        SuggestionRequestDTO suggestionRequestDTO = new SuggestionRequestDTO(fromCardId, toCardId);

        when(checkService.parseToken(tokenA)).thenReturn(fromUserId);
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(cardRepository.findByCardIdAndUser(suggestionRequestDTO.fromCardId(),fromUser))
            .thenReturn(Optional.of(fromCard));
        when(cardRepository.findActiveCardById(suggestionRequestDTO.toCardId()))
            .thenReturn(Optional.of(toCard));
        when(suggestionRepository.save(any())).thenReturn(suggestion);

        SuggestionResponseDTO expectedSuggestion = new SuggestionResponseDTO(
            suggestionId,
            suggestionType,
            fromCardId,
            toCardId,
            suggestionStatus,
            createdDate
        );

        //when
        SuggestionResponseDTO result = suggestionService.createSuggestion(
            tokenA,
            "lockName",
            suggestionType.toString(),
            suggestionRequestDTO
        );

        //then
        assertThat(result).isEqualTo(expectedSuggestion);
    }

    @Test
    @DisplayName("사용자 B는 사용자 A의 제안을 승낙하여 제안의 상태가 바뀐다.")
    public void updateSuggestionStatus() {
        //given
        Boolean isAccepted = true;
        SuggestionType suggestionType = SuggestionType.OFFER;

        when(checkService.parseToken(tokenA)).thenReturn(fromUserId);
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(cardRepository.findByCardIdAndUser(toCardId,fromUser))
            .thenReturn(Optional.of(toCard));
        when(cardRepository.findActiveCardById(fromCardId))
            .thenReturn(Optional.of(fromCard));
        when(suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard))
            .thenReturn(Optional.of(suggestion));

        SuggestionResponseDTO expectedSuggestion = new SuggestionResponseDTO(
            suggestionId,
            suggestionType,
            fromCardId,
            toCardId,
           SuggestionStatus.ACCEPTED,
            createdDate
        );

        //when
        SuggestionResponseDTO result = suggestionService.updateSuggestionStatus(
            tokenA,
            fromCardId,
            toCardId,
            isAccepted
        );

        //then
        assertThat(result).isEqualTo(expectedSuggestion);
    }

    @Test
    @DisplayName("특정 제안 유형의 제안을 조회한다.")
    public void getSuggestionsByType() {
        //given
        DirectionType directionType = DirectionType.SEND;
        SuggestionType suggestionType = SuggestionType.OFFER;
        Long cardId = fromCardId;
        String cursorId = suggestion.getCreatedDate().toString()
            .replace("T", "")
            .replace("-", "")
            .replace(":", "")
            + String.format("%08d", suggestion.getSuggestionId());
        Integer size = 10;

        when(cardRepository.findActiveCardById(cardId)).thenReturn(java.util.Optional.of(fromCard));

        when(checkService.isEqual(tokenA, fromCard.getUser().getUserId())).thenReturn(true);

        SuggestionListReadPagingResponseDTO mockResponseDTO = new SuggestionListReadPagingResponseDTO(
            List.of(new SuggestionListReadResponseDTO()),
            "nextCursorId"
        );

        when(suggestionRepository.getSuggestionsByType(
            directionType,
            suggestionType,
            fromCard.getCardId(),
            cursorId,
            size
        )).thenReturn(mockResponseDTO);

        // when
        SuggestionListReadPagingResponseDTO result = suggestionService.getSuggestionsByType(
            tokenA,
            directionType,
            suggestionType,
            cardId,
            cursorId,
            size
        );

        // then
        assertThat(result.suggestionList().size()).isEqualTo(1);
    }
}
