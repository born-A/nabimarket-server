package org.prgrms.nabimarketbe.domain.card.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.global.redisson.DistributedLock;
import org.prgrms.nabimarketbe.global.redisson.RedisDAO;
import org.prgrms.nabimarketbe.global.util.KeyGenerator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardViewCountService {
    private final RedisDAO redisDAO;

    private final CardRepository cardRepository;

    @Async("threadPoolTaskExecutor")
    @DistributedLock(key = "#lockName")
    public void increaseViewCount(
        String lockName,
        Long userId,
        Long cardId
    ) {
        // 조회수 로직 시작
        String cardViewCacheKey = KeyGenerator.generateCardViewCacheKey(cardId); // 조회수 key
        String readerCacheKey = KeyGenerator.generateCardReaderCacheKey(userId); // 유저 key

        String viewCacheValue = redisDAO.getValue(cardViewCacheKey); // 기존 조회수 값 가져오기

        if (viewCacheValue == null) {   // 조회수 정보가 캐시에 없을 경우
            int viewCountById = cardRepository.getCardViewCountById(cardId);    // db 조회
            viewCacheValue = String.valueOf(viewCountById);
            redisDAO.setValues(cardViewCacheKey, viewCacheValue);   //
        }

        int viewCount = Integer.parseInt(viewCacheValue); // 가져온 값은 Integer로 변환

        // 유저를 key로 조회한 게시글 ID List안에 해당 게시글 ID가 포함되어있지 않는다면,
        if (!redisDAO.getValuesList(readerCacheKey).contains(cardViewCacheKey)) {
            redisDAO.setValuesList(readerCacheKey, cardViewCacheKey); // 유저 key로 해당 글 ID를 List 형태로 저장
            viewCount++; // 조회수 증가
            redisDAO.setValues(cardViewCacheKey, String.valueOf(viewCount)); // 글 ID key로 조회수 저장
        }
    }

    @Transactional
    @Scheduled(cron = "0 0/10 * * * ?")
    public void flushCardViewCacheToRDB() {
        log.info("schedule start!");
        Set<String> cardViewCountKeys = redisDAO.getKeySet(KeyGenerator.CARD_VIEW_CACHE_PREFIX + "*");

        if (Objects.requireNonNull(cardViewCountKeys).isEmpty()) {
            return;
        }

        for (String cardViewCountKey : cardViewCountKeys) {
            Long cardId = Long.valueOf(cardViewCountKey.split(":")[1]);
            Integer cardViewCount = Integer.parseInt(redisDAO.getValue(cardViewCountKey));

            // DB에 캐시 데이터 반영
            cardRepository.updateViewCountByCardId(cardId, cardViewCount);

            // 캐시 데이터 삭제
            redisDAO.deleteValues(cardViewCountKey);
        }
    }
}
