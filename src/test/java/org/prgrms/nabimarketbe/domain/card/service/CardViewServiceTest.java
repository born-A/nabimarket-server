package org.prgrms.nabimarketbe.domain.card.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.nabimarketbe.global.redisson.RedisDAO;
import org.prgrms.nabimarketbe.global.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CardViewServiceTest {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private CardViewCountService cardViewCountService;

    private List<Long> userIdList = new ArrayList<>();

    private Long cardId;

    private String cardViewCacheKey;

    @BeforeEach
    void setKeys() {
        cardId = 1L;

        for (Long i = 1L; i <= 10L; i++) {
            userIdList.add(i);
        }
        cardViewCacheKey = KeyGenerator.generateCardViewCacheKey(cardId);
    }

    @AfterEach
    void deleteValues() {
        redisDAO.deleteValues(cardViewCacheKey);

        for (Long i = 1L; i <= 10; i++) {
            redisDAO.deleteValues(KeyGenerator.generateCardReaderCacheKey(i));
        }
    }

    @Test
    @DisplayName("10명의 다른 사용자가 같은 카드에 대해 단건 조회를 요청 했을 경우 조회수 증가")
    void increaseViewCountTest() throws InterruptedException {
        // given
        Integer expectedViewCountCacheValue = 10;

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            int index = i;

            executorService.submit(() -> {
                try {
                    cardViewCountService.increaseViewCount(userIdList.get(index), cardId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        taskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

        // then
        assertThat(redisDAO.getValue(cardViewCacheKey)).isEqualTo(expectedViewCountCacheValue.toString());
    }
}
