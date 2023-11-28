package org.prgrms.nabimarketbe.global.util;

public class KeyGenerator {
    public static final String CARD_VIEW_CACHE_PREFIX = "CARD-VIEW-CACHE:";

    private static final String CARD_VIEW_LOCK_PREFIX = "CARD-VIEW-LOCK:";

    private static final String CARD_READER_CACHE_PREFIX = "CARD-READER-CACHE:";

    private static final String SUGGESTION_CREATE_LOCK_PREFIX = "SUGGESTION-CREATE-LOCK:";

    public static String generateSuggestionLockKey(
        Long fromCardId,
        Long toCardId
    ) {
        Long firstId = Math.min(fromCardId, toCardId);
        Long secondId = Math.max(fromCardId, toCardId);

        return SUGGESTION_CREATE_LOCK_PREFIX + String.format("%d-%d", firstId, secondId);
    }

    public static String generateCardViewLockKey(Long cardId) {
        return CARD_VIEW_LOCK_PREFIX + cardId;
    }

    public static String generateCardViewCacheKey(Long cardId) {
        return CARD_VIEW_CACHE_PREFIX + cardId;
    }

    public static String generateCardReaderCacheKey(Long userId) {
        return CARD_READER_CACHE_PREFIX + userId;
    }
}
