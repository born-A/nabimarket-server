package org.prgrms.nabimarketbe.domain.user.service;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.dto.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.sign.UserSignupRequestDto;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.security.entity.RefreshToken;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenRequestDto;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.prgrms.nabimarketbe.oauth2.kakao.dto.KakaoProfile;
import org.prgrms.nabimarketbe.domain.user.dto.request.UserSignInRequestDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userRepository;

    private final ResponseFactory responseFactory;

    private final JwtProvider jwtProvider;

    @Transactional
    public CommonResult signInBySocial(KakaoProfile kakaoProfile) {
        CommonResult result = signIn(UserSignInRequestDTO.builder()
                .accountId(kakaoProfile.getId())
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return responseFactory.getSingleResult(result);
    }
    @Transactional
    public CommonResult signIn(UserSignInRequestDTO userSignInRequestDTO) {
        Optional<User> user = userRepository.findByAccountIdAndProvider(
                userSignInRequestDTO.accountId(),
                userSignInRequestDTO.provider()
        );

        if (user.isPresent()) {
            return responseFactory.getSingleResult(jwtProvider.createTokenDTO(
                    user.get().getUserId(), user.get().getRole())
            );
        }

        User savedUser = userRepository.save(userSignInRequestDTO.toEntity());
        return responseFactory.getSingleResult(jwtProvider.createTokenDTO(savedUser.getUserId(), savedUser.getRole()));
    }

    @Transactional
    public UserLoginResponseDTO signIn(GoogleUserInfoDTO userInfo) {
        String nameAttributeKey = userInfo.id();

        Optional<User> optionalUser = userRepository.findByNameAttributeKey(nameAttributeKey);

        User user = optionalUser.orElseGet(() -> signUp(userInfo));
        TokenResponseDTO tokenResponseDTO = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());

        UserLoginResponseDTO response = UserLoginResponseDTO.of(user, tokenResponseDTO);

        return response;
    }

    @Transactional
    public User signUp(GoogleUserInfoDTO userInfo) {
        User user = userInfo.toEntity("randomNickName");
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
