package org.prgrms.nabimarketbe.oauth2.kakao.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoProfile {
    private Long id;

    private Properties properties;

    private KakaoAccount kakaoAccount;

    @Getter
    @ToString
    public static class KakaoAccount {
        private String accountId;
    }

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
    }
}
