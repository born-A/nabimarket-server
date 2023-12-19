package org.prgrms.nabimarketbe.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.nabimarketbe.domain.user.dto.request.SocialUserInfoDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserGetResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.prgrms.nabimarketbe.setup.jwt.TokenResponseDTOBuilder;
import org.prgrms.nabimarketbe.setup.oauth2.request.SocialUserInfoDTOBuilder;
import org.prgrms.nabimarketbe.setup.user.UserBuilder;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {
    @InjectMocks
    SignService signService;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtProvider jwtProvider;

    @DisplayName("사용자는 소셜로그인 이후 받아온 정보로 나비장터에 로그인을 할 수 있다.")
    @Test
    void UserSignInTest() {
        // given
        SocialUserInfoDTO socialUserInfoDTO = SocialUserInfoDTOBuilder.createTestDTO();
        User user = UserBuilder.createTestEntity();
        TokenResponseDTO tokenResponseDTO = TokenResponseDTOBuilder.createTestDTO();
        UserGetResponseDTO userGetResponseDTO = UserGetResponseDTO.from(user);

        given(userRepository.findByAccountId(any())).willReturn(Optional.ofNullable(user));
        given(jwtProvider.createTokenDTO(any(), anyString())).willReturn(tokenResponseDTO);

        // when
        UserLoginResponseDTO loginResponseDTO = signService.signIn(socialUserInfoDTO);

        // then
        assertThat(loginResponseDTO.userInfo()).usingRecursiveComparison().isEqualTo(userGetResponseDTO);
        assertThat(loginResponseDTO.token()).usingRecursiveComparison().isEqualTo(tokenResponseDTO);
    }
}
