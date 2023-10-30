package org.prgrms.nabimarketbe.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.prgrms.nabimarketbe.domain.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.domain.user.dto.sign.UserSignupRequestDto;
import org.prgrms.nabimarketbe.domain.user.dto.sign.UserSocialLoginRequestDto;
import org.prgrms.nabimarketbe.domain.user.dto.sign.UserSocialSignupRequestDto;
import org.prgrms.nabimarketbe.domain.user.service.SignService;
import org.prgrms.nabimarketbe.domain.security.jwt.JwtProvider;
import org.prgrms.nabimarketbe.domain.security.jwt.dto.TokenResponseDto;
import org.prgrms.nabimarketbe.domain.security.oauth.dto.social.KakaoProfile;
import org.prgrms.nabimarketbe.global.model.CommonResult;
import org.prgrms.nabimarketbe.global.model.SingleResult;
import org.prgrms.nabimarketbe.global.ResponseService;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserJpaRepo;
import org.prgrms.nabimarketbe.domain.security.service.KakaoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class SignController {
    private final JwtProvider jwtProvider;

    private final UserJpaRepo userJpaRepo;

    private final KakaoService kakaoService;

    private final SignService signService;

    private final ResponseService responseService;

    @PostMapping("/social/login/kakao")
    public SingleResult<TokenResponseDto> loginByKakao(@RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(socialLoginRequestDto.accessToken());

        if (kakaoProfile == null) throw new RuntimeException("해당 회원이 없습니다.");

        User user = userJpaRepo.findByNicknameAndProvider(kakaoProfile.getProperties().getNickname(), "kakao")
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return responseService.getSingleResult(jwtProvider.createTokenDto(user.getUserId(), user.getRoles()));
    }

    @PostMapping("/social/signup/kakao")
    public CommonResult signupBySocial(@RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(socialSignupRequestDto.accessToken());

        if (kakaoProfile == null) throw new RuntimeException("해당 회원이 없습니다.");

        Long userId = signService.socialSignup(UserSignupRequestDto.builder()
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return responseService.getSingleResult(userId);
    }

    //엑세스 토큰 만료시 회원 검증 후 리프레쉬 토큰을 검증해서 액세스 토큰과 리프레시 토큰을 재발급
    @PostMapping("/reissue")
    public SingleResult<TokenResponseDto> reissue(
            @RequestBody TokenRequestDto tokenRequestDto) {
        return responseService.getSingleResult(signService.reissue(tokenRequestDto));
    }
}
