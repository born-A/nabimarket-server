package org.prgrms.nabimarketbe.completeRequest.service;

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
import org.prgrms.nabimarketbe.completeRequest.dto.request.CompleteRequestDTO;
import org.prgrms.nabimarketbe.completeRequest.dto.response.CompleteRequestResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.entity.CompleteRequestStatus;
import org.prgrms.nabimarketbe.completeRequest.repository.CompleteRequestRepository;
import org.prgrms.nabimarketbe.completeRequest.wrapper.CompleteRequestInfoDTO;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.suggestion.repository.SuggestionRepository;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.completeRequest.entity.CompleteRequest;
import org.prgrms.nabimarketbe.user.repository.UserRepository;
import org.prgrms.nabimarketbe.user.service.CheckService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompleteRequestServiceTest {
    @InjectMocks
    private CompleteRequestService completeRequestService;

    @Mock
    private CompleteRequestRepository completeRequestRepository;

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CheckService checkService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private String tokenA;
    private String tokenB;
    private Long fromUserId;
    private Long toUserId;
    private Long toCardId;
    private Long fromCardId;
    private Long completeRequestId;
    private LocalDateTime createdDate;
    private User fromUser;
    private User toUser;

    private Card toCard;
    private Card fromCard;
    private Suggestion suggestion;
    private CompleteRequest completeRequest;


    @BeforeEach
    public void setUp() {
        tokenA = "tokenA";
        tokenB = "tokenB";

        SuggestionType suggestionType = SuggestionType.OFFER;

        fromUserId = 1L;
        toUserId = 2L;

        toCardId = 1L;
        fromCardId = 2L;

        Long suggestionId = 1L;
        completeRequestId = 1L;

        createdDate = LocalDateTime.now();

        fromUser = User.builder()
            .role("USER")
            .provider("GOOGLE")
            .nickname("A")
            .imageUrl("test_url")
            .accountId("test_B_Id")
            .build();

        ReflectionTestUtils.setField(fromUser, "userId", fromUserId);

        toUser = User.builder()
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


        completeRequest = new CompleteRequest(fromCard, toCard);
        ReflectionTestUtils.setField(completeRequest, "completeRequestId", completeRequestId);
        ReflectionTestUtils.setField(completeRequest, "createdDate", createdDate);
    }

    @Test
    @DisplayName("거래 성사 요청이 생성된다.")
    public void createCompleteRequest() {
        //given
        CompleteRequestDTO completeRequestDTO = new CompleteRequestDTO(
            fromCardId,
            toCardId
        );

        when(checkService.parseToken(tokenA)).thenReturn(fromUserId);
        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(cardRepository.findByCardIdAndUser(completeRequestDTO.fromCardId(),fromUser))
            .thenReturn(Optional.of(fromCard));
        when(cardRepository.findActiveCardById(completeRequestDTO.toCardId())).thenReturn(Optional.of(toCard));
        when(suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard))
            .thenReturn(Optional.of(suggestion));
        when(completeRequestRepository.exists(fromCard, toCard)).thenReturn(false);
        when(completeRequestRepository.existsByFromCard(fromCard)).thenReturn(false);
        when(completeRequestRepository.existsByToCard(toCard)).thenReturn(false);
        when(completeRequestRepository.save(any())).thenReturn(completeRequest);

        suggestion.decideSuggestion(true);

        CompleteRequestResponseDTO intended = new CompleteRequestResponseDTO(
            completeRequestId,
            fromCardId,
            toCardId,
            CompleteRequestStatus.WAITING,
            createdDate
        );

        //when
        CompleteRequestResponseDTO result = completeRequestService.createCompleteRequest(
            tokenA,
            completeRequestDTO
        );

        //then
        assertThat(result).isEqualTo(intended);
    }

    @Test
    @DisplayName("사용자 B가 거래 성사 요청을 수락하여 거래 성사 요청의 상태가 바뀐다.")
    public void updateSuggestionStatus() {
        //given
        Boolean isAccepted = true;

        when(checkService.parseToken(tokenB)).thenReturn(toUserId);
        when(checkService.isEqual(toUserId, fromCard.getUser().getUserId())).thenReturn(true);
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(toUser));
        when(cardRepository.findExistingCardById(fromCardId)).thenReturn(Optional.ofNullable(fromCard));
        when(cardRepository.findByCardIdAndUser(toCardId, toUser)).thenReturn(Optional.ofNullable(toCard));
        when(completeRequestRepository.findCompleteRequestByFromCardAndToCard(fromCard, toCard)).thenReturn(Optional.ofNullable(completeRequest));

        CompleteRequestResponseDTO intended = new CompleteRequestResponseDTO(
            completeRequestId,
            fromCardId,
            toCardId,
            CompleteRequestStatus.ACCEPTED,
            createdDate
        );

        //when
        CompleteRequestResponseDTO result = completeRequestService.updateCompleteRequestStatus(
            tokenB,
            fromCardId,
            toCardId,
            isAccepted
        );

        //then
        assertThat(result).isEqualTo(intended);
    }

    @Test
    @DisplayName("거래 성사 내역을 id로 조회할 수 있다.")
    public void getCompleteRequestById() {
        //given
        when(checkService.parseToken(tokenA)).thenReturn(fromUserId);
        when(userRepository.existsById(fromUserId)).thenReturn(true);
        when(completeRequestRepository.findById(completeRequestId))
            .thenReturn(Optional.ofNullable(completeRequest));

        //when
        CompleteRequestInfoDTO result = completeRequestService.getCompleteRequestById(tokenA, completeRequestId);

        //then
        assertNotNull(result);
    }
}