package user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.nabimarketbe.jwt.provider.JwtProvider;
import org.prgrms.nabimarketbe.user.service.CheckService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

@ExtendWith(MockitoExtension.class)
class CheckServiceTest {
    @InjectMocks
    CheckService checkService;

    @Mock
    JwtProvider jwtProvider;

    @DisplayName("parseToken 메소드를 호출하면 유저의 Id가 파싱된다.")
    @Test
    void parseToken_test() {
        // given
        Long expectedUserId = 1L;
        String token = "1L";
        Claims claims = new DefaultClaims();
        claims.put("sub", expectedUserId);

        given(jwtProvider.parseClaims(any())).willReturn(claims);

        // when
        Long userId = checkService.parseToken(token);

        // then
        assertThat(userId).isEqualTo(expectedUserId);
    }

    @DisplayName("String(유저토큰)과 Long(유저Id)이 같은지 비교한다.")
    @Test
    void isEqual_String_Long_test() {
        // given
        Long expectedUserId = 1L;
        String token = "1L";
        Claims claims = new DefaultClaims();
        claims.put("sub", expectedUserId);

        given(jwtProvider.parseClaims(any())).willReturn(claims);

        // when & then
        assertThat(checkService.isEqual(token, expectedUserId)).isTrue();
    }

    @DisplayName("Long(유저1 Id)과 Long(유저2 Id)이 같은지 비교한다.")
    @Test
    void isEqual_Long_Long_test() {
        // given
        Long userOneId = 1L;
        Long userOneId2 = 1L;

        // when & then
        assertThat(checkService.isEqual(userOneId, userOneId2)).isTrue();
    }

}
