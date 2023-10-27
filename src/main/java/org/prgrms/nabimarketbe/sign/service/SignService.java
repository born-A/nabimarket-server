package org.prgrms.nabimarketbe.sign.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.nabimarketbe.config.security.JwtProvider;
import org.prgrms.nabimarketbe.config.security.domain.RefreshToken;
import org.prgrms.nabimarketbe.config.security.domain.RefreshTokenJpaRepo;
import org.prgrms.nabimarketbe.config.security.jwt.dto.TokenDto;
import org.prgrms.nabimarketbe.config.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.global.exception.CEmailLoginFailedException;
import org.prgrms.nabimarketbe.global.exception.CEmailSignupFailedException;
import org.prgrms.nabimarketbe.global.exception.CRefreshTokenException;
import org.prgrms.nabimarketbe.global.exception.CUserExistException;
import org.prgrms.nabimarketbe.global.exception.CUserNotFoundException;
import org.prgrms.nabimarketbe.sign.dto.UserLoginRequestDto;
import org.prgrms.nabimarketbe.sign.dto.UserSignupRequestDto;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserJpaRepo;
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
//        User user = userJpaRepo.findByEmail(userLoginRequestDto.getEmail())
//                .orElseThrow(CEmailLoginFailedException::new);

        User user = userJpaRepo.findByNickname(userLoginRequestDto.getNickname())
                .orElseThrow(CEmailLoginFailedException::new);

        // 회원 패스워드 일치 여부 확인
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword()))
            throw new CEmailLoginFailedException();

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
//        if (userJpaRepo.findByEmail(userSignupDto.getEmail()).isPresent())
//            throw new CEmailSignupFailedException();
        if (userJpaRepo.findByNickname(userSignupDto.getNickname()).isPresent())
            throw new CEmailSignupFailedException();
        return userJpaRepo.save(userSignupDto.toEntity(passwordEncoder)).getUserId();
    }

    @Transactional
    public Long socialSignup(UserSignupRequestDto userSignupRequestDto) {
//        if (userJpaRepo
//                .findByEmailAndProvider(userSignupRequestDto.getEmail(), userSignupRequestDto.getProvider())
//                .isPresent()
        if (userJpaRepo
                .findByNicknameAndProvider(userSignupRequestDto.getNickname(), userSignupRequestDto.getProvider())
                .isPresent()
        ) throw new CUserExistException();
        return userJpaRepo.save(userSignupRequestDto.toEntity()).getUserId();
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 만료된 refresh token 에러
        if (!jwtProvider.validationToken(tokenRequestDto.getRefreshToken())) {
            throw new CRefreshTokenException();
        }

        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        User user = userJpaRepo.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(CUserNotFoundException::new);
        RefreshToken refreshToken = tokenJpaRepo.findByKey(user.getUserId())
                .orElseThrow(CRefreshTokenException::new);

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new CRefreshTokenException();

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        tokenJpaRepo.save(updateRefreshToken);

        return newCreatedToken;
    }
}
