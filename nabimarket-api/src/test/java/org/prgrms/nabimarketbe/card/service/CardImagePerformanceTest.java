package org.prgrms.nabimarketbe.card.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.card.repository.CardRepository;
import org.prgrms.nabimarketbe.cardImage.entity.CardImage;
import org.prgrms.nabimarketbe.cardImage.repository.CardImageBatchRepository;
import org.prgrms.nabimarketbe.cardImage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.category.entity.Category;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CardImagePerformanceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CardRepository cardRepository;


    @Autowired
    private CardImageRepository cardImageRepository;

    @Autowired
    private CardImageBatchRepository cardImageBatchRepository;

    @Test
    @DisplayName("Batch Insert 적용 후 속도 향상 테스트")
    @Transactional
    void uploadCardImages() {
        // given
        User user = User.builder()
            .accountId("108146083589124654466")
            .nickname("당돌한 루피")
            .imageUrl("xxx")
            .provider("google")
            .role("USER")
            .build();
        userRepository.save(user);

        Category category = new Category(CategoryEnum.ELECTRONICS);
        categoryRepository.save(category);

        Item item = Item.builder()
            .itemName("xxx")
            .priceRange(PriceRange.PRICE_RANGE_ONE)
            .category(category)
            .build();
        itemRepository.save(item);

        Card card = Card.builder()
            .cardTitle("xxx")
            .content("xxx")
            .thumbnail("xxx")
            .tradeArea("xxx")
            .pokeAvailable(true)
            .tradeType(TradeType.DIRECT_DEALING)
            .item(item)
            .user(user)
            .build();
        cardRepository.save(card);

        int maximumCardImageCount = 10; // 최대 업로드 이미지 파일 갯수
        List<CardImage> cardImages = new ArrayList<>();
        for (int i = 0; i < maximumCardImageCount; i++) {
            cardImages.add(
                CardImage.builder()
                    .imageUrl("xxx1")
                    .card(card)
                    .build()
            );
        }

        // when
        long start1 = System.currentTimeMillis();
        cardImageRepository.saveAll(cardImages);
        long end1 = System.currentTimeMillis();

        long start2 = System.currentTimeMillis();
        cardImageBatchRepository.saveAll(cardImages);
        long end2 = System.currentTimeMillis();

        // then
        long requiredTimeForInsert = end1 - start1;         // batch insert를 적용하지 않은 insert 소요 시간
        long requiredTimeForBatchInsert = end2 - start2;    // batch insert를 적용한 insert 소요 시간

        assertThat(requiredTimeForBatchInsert).isLessThan(requiredTimeForInsert);
    }
}