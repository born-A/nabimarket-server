package org.prgrms.nabimarketbe.domain.sign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.nabimarketbe.domain.sign.dto.UserLoginRequestDto;
import org.prgrms.nabimarketbe.domain.sign.dto.UserSignupRequestDto;
import org.prgrms.nabimarketbe.domain.security.jwt.JwtProvider;
import org.prgrms.nabimarketbe.domain.security.entity.RefreshToken;
import org.prgrms.nabimarketbe.domain.security.repository.RefreshTokenJpaRepo;
import org.prgrms.nabimarketbe.domain.security.jwt.dto.TokenDto;
import org.prgrms.nabimarketbe.domain.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserJpaRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserJpaRepo userJpaRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final RefreshTokenJpaRepo tokenJpaRepo;

    @Transactional
    public TokenDto login(UserLoginRequestDto userLoginRequestDto) {
        // 회원 정보 존재하는지 확인
        User user = userJpaRepo.findByNickname(userLoginRequestDto.getNickname())
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();

        tokenJpaRepo.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public Long signup(UserSignupRequestDto userSignupDto) {
        if (userJpaRepo.findByNickname(userSignupDto.getNickname()).isPresent())
            throw new RuntimeException("이미 존재하는 회원입니다.");

        return userJpaRepo.save(userSignupDto.toEntity()).getUserId();
    }

    @Transactional
    public Long socialSignup(UserSignupRequestDto userSignupRequestDto) {
        if (userJpaRepo
                .findByNicknameAndProvider(userSignupRequestDto.getNickname(), userSignupRequestDto.getProvider())
                .isPresent()
        ) throw new RuntimeException("이미 존재하는 회원입니다.");

        return userJpaRepo.save(userSignupRequestDto.toEntity()).getUserId();
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 만료된 refresh token 에러
        if (!jwtProvider.validationToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("RefreshTokenException");
        }

        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        User user = userJpaRepo.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() ->new RuntimeException("RefreshTokenException"));

        RefreshToken refreshToken = tokenJpaRepo.findByKey(user.getUserId())
                .orElseThrow(() ->new RuntimeException("RefreshTokenException"));

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new RuntimeException("RefreshTokenException");

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());

        tokenJpaRepo.save(updateRefreshToken);

        return newCreatedToken;
    }
}
