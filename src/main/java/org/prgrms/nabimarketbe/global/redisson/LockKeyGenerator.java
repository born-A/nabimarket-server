package org.prgrms.nabimarketbe.global.redisson;

public class LockKeyGenerator {
    public static String generateSuggestionKey(
        Long fromCardId,
        Long toCardId
    ) {
        Long firstId = Math.min(fromCardId, toCardId);
        Long secondId = Math.max(fromCardId, toCardId);

        String key = String.format("%d-%d", firstId, secondId);

        return key;
    }
}
