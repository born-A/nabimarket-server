package org.prgrms.nabimarketbe.domain.sign.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.nabimarketbe.domain.sign.dto.UserLoginRequestDto;
import org.prgrms.nabimarketbe.domain.sign.dto.UserSignupRequestDto;
import org.prgrms.nabimarketbe.domain.sign.dto.UserSocialLoginRequestDto;
import org.prgrms.nabimarketbe.domain.sign.dto.UserSocialSignupRequestDto;
import org.prgrms.nabimarketbe.domain.sign.service.SignService;
import org.prgrms.nabimarketbe.domain.security.jwt.JwtProvider;
import org.prgrms.nabimarketbe.domain.security.jwt.dto.TokenDto;
import org.prgrms.nabimarketbe.domain.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.domain.security.oauth.dto.social.KakaoProfile;
import org.prgrms.nabimarketbe.global.model.CommonResult;
import org.prgrms.nabimarketbe.global.model.SingleResult;
import org.prgrms.nabimarketbe.domain.security.oauth.service.response.ResponseService;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserJpaRepo;
import org.prgrms.nabimarketbe.domain.user.service.KakaoService;
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

    @PostMapping("/login")
    public SingleResult<TokenDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        TokenDto tokenDto = signService.login(userLoginRequestDto);

        return responseService.getSingleResult(tokenDto);
    }

    @PostMapping("/signup")
    public SingleResult<Long> signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {
        Long signupId = signService.signup(userSignupRequestDto);

        return responseService.getSingleResult(signupId);
    }

    @PostMapping("/reissue")
    public SingleResult<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return responseService.getSingleResult(signService.reissue(tokenRequestDto));
    }

    @PostMapping("/social/login/kakao")
    public SingleResult<TokenDto> loginByKakao(@RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(socialLoginRequestDto.getAccessToken());

        if (kakaoProfile == null) throw new RuntimeException("해당 회원이 없습니다.");

        User user = userJpaRepo.findByNicknameAndProvider(kakaoProfile.getProperties().getNickname(), "kakao")
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        return responseService.getSingleResult(jwtProvider.createTokenDto(user.getUserId(), user.getRoles()));
    }

    @PostMapping("/social/signup/kakao")
    public CommonResult signupBySocial(@RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {
        KakaoProfile kakaoProfile =
                kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());

        if (kakaoProfile == null) throw new RuntimeException("해당 회원이 없습니다.");

        Long userId = signService.socialSignup(UserSignupRequestDto.builder()
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return responseService.getSingleResult(userId);
    }
}
