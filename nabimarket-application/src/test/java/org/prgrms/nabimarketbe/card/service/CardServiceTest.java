package org.prgrms.nabimarketbe.card.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.nabimarketbe.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.CardDetailResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.CardUpdateResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.wrapper.CardResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.wrapper.CardUserResponseDTO;
import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.CardStatus;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.card.repository.CardRepository;
import org.prgrms.nabimarketbe.cardImage.entity.CardImage;
import org.prgrms.nabimarketbe.cardImage.repository.CardImageBatchRepository;
import org.prgrms.nabimarketbe.cardImage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.cardimage.dto.request.CardImageCreateRequestDTO;
import org.prgrms.nabimarketbe.cardimage.dto.response.CardImageCreateResponseDTO;
import org.prgrms.nabimarketbe.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.cardimage.dto.response.CardImageUpdateResponseDTO;
import org.prgrms.nabimarketbe.category.entity.Category;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.user.dto.response.UserSummaryResponseDTO;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserRepository;
import org.prgrms.nabimarketbe.user.service.CheckService;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @InjectMocks
    private CardService cardService;

    @Mock
    private CheckService checkService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CardImageRepository cardImageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardImageBatchRepository cardImageBatchRepository;

    private static String token;
    private static Long userId;
    private static Long cardId;
    private static LocalDateTime createdDate;
    private static LocalDateTime modifiedDate;
    private static User user;
    private static Category category;
    private static Item item;
    private static Card card;
    private static List<CardImage> images;

    @BeforeEach
    void setUp() {
        token = "token";
        userId = 1L;
        cardId = 1L;
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        user = User.builder()
            .role("USER")
            .provider("GOOGLE")
            .nickname("doby")
            .imageUrl("img_url")
            .accountId("1010110")
            .build();
        ReflectionTestUtils.setField(user, "userId", userId);
        category = new Category(CategoryEnum.ELECTRONICS);
        item = Item.builder()
            .priceRange(PriceRange.PRICE_RANGE_FOUR)
            .itemName("apple macbook pro")
            .category(category)
            .build();
        card = Card.builder()
            .cardTitle("macbook pro 교환 원해요")
            .content("상태 최상입니다.")
            .tradeType(TradeType.DIRECT_DEALING)
            .pokeAvailable(true)
            .tradeArea("서울")
            .thumbnail("thumbnail_img_url")
            .user(user)
            .item(item)
            .build();
        ReflectionTestUtils.setField(card, "cardId", cardId);
        ReflectionTestUtils.setField(card, "createdDate", createdDate);
        ReflectionTestUtils.setField(card, "modifiedDate", modifiedDate);

        images = Arrays.asList(
            new CardImage("url1", card),
            new CardImage("url2", card),
            new CardImage("url3", card)
        );
    }
}