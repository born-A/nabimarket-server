package org.prgrms.nabimarketbe.sign.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.nabimarketbe.config.security.JwtProvider;
import org.prgrms.nabimarketbe.config.security.jwt.dto.TokenDto;
import org.prgrms.nabimarketbe.config.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.oauth.dto.social.KakaoProfile;
import org.prgrms.nabimarketbe.global.exception.CUserNotFoundException;
import org.prgrms.nabimarketbe.global.model.CommonResult;
import org.prgrms.nabimarketbe.global.model.SingleResult;
import org.prgrms.nabimarketbe.oauth.service.response.ResponseService;
import org.prgrms.nabimarketbe.sign.dto.UserLoginRequestDto;
import org.prgrms.nabimarketbe.sign.dto.UserSignupRequestDto;
import org.prgrms.nabimarketbe.sign.dto.UserSocialLoginRequestDto;
import org.prgrms.nabimarketbe.sign.dto.UserSocialSignupRequestDto;
import org.prgrms.nabimarketbe.sign.service.SignService;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserJpaRepo;
import org.prgrms.nabimarketbe.user.service.KakaoService;
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
    public SingleResult<TokenDto> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto) {

        TokenDto tokenDto = signService.login(userLoginRequestDto);
        return responseService.getSingleResult(tokenDto);
    }

    @PostMapping("/signup")
    public SingleResult<Long> signup(
            @RequestBody UserSignupRequestDto userSignupRequestDto) {
        Long signupId = signService.signup(userSignupRequestDto);
        return responseService.getSingleResult(signupId);
    }

    @PostMapping("/reissue")
    public SingleResult<TokenDto> reissue(
            @RequestBody TokenRequestDto tokenRequestDto) {
        return responseService.getSingleResult(signService.reissue(tokenRequestDto));
    }

    @PostMapping("/social/login/kakao")
    public SingleResult<TokenDto> loginByKakao(
            @RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {

        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(socialLoginRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();

//        User user = userJpaRepo.findByEmailAndProvider(kakaoProfile.getKakao_account().getEmail(), "kakao")
//                .orElseThrow(CUserNotFoundException::new);
        User user = userJpaRepo.findByNicknameAndProvider(kakaoProfile.getProperties().getNickname(), "kakao")
                .orElseThrow(CUserNotFoundException::new);

        return responseService.getSingleResult(jwtProvider.createTokenDto(user.getUserId(), user.getRoles()));
    }

    @PostMapping("/social/signup/kakao")
    public CommonResult signupBySocial(
            @RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {

        KakaoProfile kakaoProfile =
                kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CUserNotFoundException();
//        if (kakaoProfile.getKakao_account().getEmail() == null) {
//            kakaoService.kakaoUnlink(socialSignupRequestDto.getAccessToken());
//            throw new CSocialAgreementException();
//        }

        Long userId = signService.socialSignup(UserSignupRequestDto.builder()
//                .email(kakaoProfile.getKakao_account().getEmail())
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return responseService.getSingleResult(userId);
    }
}
