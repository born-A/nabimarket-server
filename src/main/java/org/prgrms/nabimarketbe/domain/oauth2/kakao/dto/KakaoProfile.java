package org.prgrms.nabimarketbe.domain.oauth2.kakao.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoProfile {
    private String id;

    private Properties properties;

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
    }
}
