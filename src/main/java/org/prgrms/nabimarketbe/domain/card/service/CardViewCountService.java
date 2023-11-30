package org.prgrms.nabimarketbe.domain.card.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.global.redisson.RedisDAO;
import org.prgrms.nabimarketbe.global.util.KeyGenerator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseViewCount(
        Long userId,
        Long cardId
    ) {
        // 조회수 로직 시작
        String cardViewCacheKey = KeyGenerator.generateCardViewCacheKey(cardId); // 조회수 key
        String readerCacheKey = KeyGenerator.generateCardReaderCacheKey(userId); // 유저 key

        // 유저를 key로 조회한 게시글 ID List안에 해당 게시글 ID가 포함되어있지 않는다면,
        if (!redisDAO.getValuesList(readerCacheKey).contains(cardViewCacheKey)) {
            // TODO: 캐시 데이터 삭제 주기 고려해보기
            redisDAO.setValuesList(readerCacheKey, cardViewCacheKey);   // 유저 key로 해당 글 ID를 List 형태로 저장

            redisDAO.increment(cardViewCacheKey); // 조회수 증가
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

        for (String cardViewCountKey : cardViewCountKeys) { // TODO: 추후에 bulk update로 전환
            Long cardId = Long.valueOf(cardViewCountKey.split(":")[1]);
            Integer cardViewAdd = Integer.parseInt(redisDAO.getValue(cardViewCountKey));    // 조회수 증가값

            // DB에 캐시 데이터 반영
            Integer cardViewCount = cardRepository.getCardViewCountById(cardId);    // 기존 조회수 값
            cardViewCount = cardViewCount + cardViewAdd;    // 조회수 증가 적용
            cardRepository.updateViewCountByCardId(cardId, cardViewCount);

            // 캐시 데이터 삭제
            redisDAO.deleteValues(cardViewCountKey);
        }
    }
}
