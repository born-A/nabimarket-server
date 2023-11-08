package org.prgrms.nabimarketbe.domain.card.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.prgrms.nabimarketbe.config.TestConfig;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    private static Long userChoiId = 1L;

    @BeforeAll
    void suggestionUserSetting() {
        User userChoi = User.builder()
                .accountId("c")
                .nickname("choi")
                .imageUrl("xxx")
                .provider("KAKAO")
                .role("ROLE_USER")
                .build();
        ReflectionTestUtils.setField(userChoi, "userId", userChoiId);
        userRepository.save(userChoi);

        Category category = new Category(CategoryEnum.ELECTRONICS);
        categoryRepository.save(category);

        // under Item
        Item itemFromChoiUnder = Item.builder()
                .itemName("lg pad")
                .priceRange(PriceRange.PRICE_RANGE_THREE)
                .category(category)
                .build();

        Card cardFromChoiUnder = Card.builder()
                .cardTitle("choi's card")
                .thumbNailImage("xxx")
                .content("xxx")
                .tradeArea("seoul")
                .poke(true)
                .tradeType(TradeType.DIRECT_DEALING)
                .item(itemFromChoiUnder)
                .user(userChoi)
                .build();
        ReflectionTestUtils.setField(cardFromChoiUnder, "cardId", 1L);

        itemRepository.save(itemFromChoiUnder);
        cardRepository.save(cardFromChoiUnder);

        // Over Item
        Item itemFromChoiOver = Item.builder()
                .itemName("lg TV")
                .priceRange(PriceRange.PRICE_RANGE_FIVE)
                .category(category)
                .build();

        Card cardFromChoiOver = Card.builder()
                .cardTitle("choi's card")
                .thumbNailImage("xxx")
                .content("xxx")
                .tradeArea("seoul")
                .poke(true)
                .tradeType(TradeType.DIRECT_DEALING)
                .item(itemFromChoiOver)
                .user(userChoi)
                .build();
        ReflectionTestUtils.setField(cardFromChoiOver, "cardId", 2L);

        itemRepository.save(itemFromChoiOver);
        cardRepository.save(cardFromChoiOver);
    }

    @Test
    void 제안_가능한_카드_조회_찔러보기_허용인_경우() {
        // given
        Long suggestionUserId = userChoiId; // 제안하고 있는 유저 id

        // 제안 대상이 되는 카드의 정보
        PriceRange targetCardPriceRange = PriceRange.PRICE_RANGE_FOUR;
        Boolean targetCardPokeAvailable = true; // 찔러보기 혀용

        // 반환 예상 값
        List<SuggestionAvailableCardResponseDTO> expectCardList = new ArrayList<>();
        SuggestionAvailableCardResponseDTO card1 = new SuggestionAvailableCardResponseDTO();
        ReflectionTestUtils.setField(card1, "cardId", 1L);
        ReflectionTestUtils.setField(card1, "thumbNail", "xxx");
        ReflectionTestUtils.setField(card1, "itemName", "lg pad");
        ReflectionTestUtils.setField(card1, "priceRange", PriceRange.PRICE_RANGE_THREE);
        ReflectionTestUtils.setField(card1, "suggestionType", SuggestionType.POKE);

        SuggestionAvailableCardResponseDTO card2 = new SuggestionAvailableCardResponseDTO();
        ReflectionTestUtils.setField(card2, "cardId", 2L);
        ReflectionTestUtils.setField(card2, "thumbNail", "xxx");
        ReflectionTestUtils.setField(card2, "itemName", "lg TV");
        ReflectionTestUtils.setField(card2, "priceRange", PriceRange.PRICE_RANGE_FIVE);
        ReflectionTestUtils.setField(card2, "suggestionType", SuggestionType.OFFER);

        expectCardList.add(card1);
        expectCardList.add(card2);

        // when
        List<SuggestionAvailableCardResponseDTO> actualCardList = cardRepository.getSuggestionAvailableCards(
                suggestionUserId,
                targetCardPriceRange,
                targetCardPokeAvailable
        );

        // then
        assertThat(expectCardList.size()).isEqualTo(actualCardList.size());

        for (int i = 0; i < actualCardList.size(); i++) {
            assertThat(expectCardList.get(i))
                    .usingRecursiveComparison()
                    .isEqualTo(actualCardList.get(i));
        }
    }

    @Test
    void 제안_가능한_카드_조회_찔러보기_불가인_경우() {
        // given
        Long suggestionUserId = userChoiId; // 제안하고 있는 유저 id

        // 제안 대상이 되는 카드의 정보
        PriceRange targetCardPriceRange = PriceRange.PRICE_RANGE_FOUR;
        Boolean targetCardPokeAvailable = false;    // 찔러보기 불가

        // 반환 예상 값
        List<SuggestionAvailableCardResponseDTO> expectCardList = new ArrayList<>();

        SuggestionAvailableCardResponseDTO card1 = new SuggestionAvailableCardResponseDTO();
        ReflectionTestUtils.setField(card1, "cardId", 2L);
        ReflectionTestUtils.setField(card1, "thumbNail", "xxx");
        ReflectionTestUtils.setField(card1, "itemName", "lg TV");
        ReflectionTestUtils.setField(card1, "priceRange", PriceRange.PRICE_RANGE_FIVE);
        ReflectionTestUtils.setField(card1, "suggestionType", SuggestionType.OFFER);

        expectCardList.add(card1);

        // when
        List<SuggestionAvailableCardResponseDTO> actualCardList = cardRepository.getSuggestionAvailableCards(
                suggestionUserId,
                targetCardPriceRange,
                targetCardPokeAvailable
        );

        // then
        assertThat(expectCardList.size()).isEqualTo(actualCardList.size());

        for (int i = 0; i < actualCardList.size(); i++) {
            assertThat(expectCardList.get(i))
                    .usingRecursiveComparison()
                    .isEqualTo(actualCardList.get(i));
        }
    }
}