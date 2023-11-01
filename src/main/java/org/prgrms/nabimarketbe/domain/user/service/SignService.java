package org.prgrms.nabimarketbe.domain.user.service;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.dto.sign.UserSignupRequestDto;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.security.entity.RefreshToken;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleUserInfoDTO;
import org.prgrms.nabimarketbe.oauth2.kakao.dto.KakaoProfile;
import org.prgrms.nabimarketbe.oauth2.kakao.repository.RefreshTokenJpaRepo;
import org.prgrms.nabimarketbe.oauth2.kakao.service.OAuth2Service;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userJpaRepo;

    private final OAuth2Service oAuth2Service;

    private final ResponseFactory responseFactory;

    private final JwtProvider jwtProvider;

    private final RefreshTokenJpaRepo tokenJpaRepo;

    private final RandomNicknameGenerator randomNicknameGenerator;

    @Transactional
    public CommonResult signupBySocial(String accessToken) {
        KakaoProfile kakaoProfile = oAuth2Service.getKakaoProfile(accessToken);

        if (kakaoProfile == null) throw new RuntimeException("카카오에 해당 회원이 없습니다.");

        CommonResult result = socialSignup(UserSignupRequestDto.builder()
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return responseFactory.getSingleResult(result);
    }

    @Transactional
    public CommonResult socialSignup(UserSignupRequestDto userSignupRequestDto) {
        Optional<User> user = userJpaRepo.findByNicknameAndProvider(
                userSignupRequestDto.nickname(),
                userSignupRequestDto.provider()
        );

        if (user.isPresent()) {
            return responseFactory.getSingleResult(jwtProvider.createTokenDto(user.get().getUserId(), user.get().getRoles()));
        }

        userJpaRepo.save(userSignupRequestDto.toEntity());
        return responseFactory.getSingleResult(jwtProvider.createTokenDto(user.get().getUserId(), user.get().getRoles()));
    }

    @Transactional
    public TokenResponseDTO reissue(TokenRequestDto tokenRequestDto) {
        // 만료된 refresh token 에러
        if (!jwtProvider.validationToken(tokenRequestDto.getAccessToken())) {
            throw new RuntimeException("RefreshTokenException");
        }

        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        User user = userJpaRepo.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() ->new RuntimeException("RefreshTokenException"));

        RefreshToken refreshToken = tokenJpaRepo.findByUserId(user.getUserId())
                .orElseThrow(() ->new RuntimeException("RefreshTokenException"));

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new RuntimeException("RefreshTokenException");

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenResponseDTO newCreatedToken = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());

        tokenJpaRepo.save(updateRefreshToken);

        return newCreatedToken;
    }

    @Transactional
    public User signUp(GoogleUserInfoDTO googleUserInfoDTO) throws JsonProcessingException {
        String randomNickname = randomNicknameGenerator.generateRandomNickname();
        User user = googleUserInfoDTO.toEntity(randomNickname);
        User savedUser = userJpaRepo.save(user);

        return savedUser;
    }
}
