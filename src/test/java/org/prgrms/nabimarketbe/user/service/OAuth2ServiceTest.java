package org.prgrms.nabimarketbe.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prgrms.nabimarketbe.oauth2.kakao.service.OAuth2Service;
import org.prgrms.nabimarketbe.oauth2.kakao.dto.KakaoProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(username = "mockUser")
public class OAuth2ServiceTest {

    @Autowired
    private OAuth2Service OAuth2Service;

    @Autowired
    Environment env;

    private static String accessToken;

    @BeforeEach
    public void setToken() {
        accessToken = env.getProperty("social.kakao.accessToken");
    }

    @Test
    public void 액세스토큰으로_사용자정보_요청() throws Exception
    {
        //given
        //when
        KakaoProfile kakaoProfile = OAuth2Service.getKakaoProfile(accessToken);

        //then
        assertThat(kakaoProfile).isNotNull();
        assertThat(kakaoProfile.getProperties().getNickname()).isEqualTo("신예진");
    }
}