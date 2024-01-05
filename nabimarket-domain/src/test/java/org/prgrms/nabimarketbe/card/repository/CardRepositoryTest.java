package org.prgrms.nabimarketbe.card.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.nabimarketbe.card.projection.CardPagingResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
@Import(TestConfig.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan("org.prgrms.nabimarketbe.*")
class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;

    @DisplayName("커서 기반 페이지네이션의 실행시간을 측정한다.")
    @Test
    void cursor_test() {
        // given
        long startTime = System.currentTimeMillis();
        int size = 1000;
        Sort sort = Sort.by(
            Sort.Order.desc("createdDate"),
            Sort.Order.desc("cardId")
        );

        PageRequest pageRequest = PageRequest.of(
            0,
            size,
            sort
        );

        // when
        CardPagingResponseDTO cardsByCondition = cardRepository.getCardsByCondition(
            null,
            null,
            null,
            null,
            "2024010207403200033916",
            pageRequest
        );

        // then
        long endTime = System.currentTimeMillis();

        log.info(cardsByCondition.nextCursorId());
        log.info("{}", endTime - startTime);
    }

    @DisplayName("오프셋 기반 페이지네이션의 실행시간을 측정한다.")
    @Test
    void offset_test() {
        // given
        long startTime = System.currentTimeMillis();
        int size = 1000;
        Sort sort = Sort.by(
            Sort.Order.desc("createdDate"),
            Sort.Order.desc("cardId")
        );

        PageRequest pageRequest = PageRequest.of(
            507,
            size,
            sort
        );

        // when
        CardPagingResponseDTO cardsByCondition = cardRepository.getCardsByConditionAndOffset(
            null,
            null,
            null,
            null,
            pageRequest
        );

        // then
        long endTime = System.currentTimeMillis();

        log.info("{}", endTime - startTime);
    }
}
