package org.prgrms.nabimarketbe.domain.security.oauth.dto.social;

import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoProfile {
    private Long id;

    private Properties properties;

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
    }
}
