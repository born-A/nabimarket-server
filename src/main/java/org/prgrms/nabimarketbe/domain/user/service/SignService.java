package org.prgrms.nabimarketbe.domain.user.service;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserSignInRequestDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleUserInfoDTO;
import org.prgrms.nabimarketbe.oauth2.kakao.dto.KakaoProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    
    private final RandomNicknameGenerator randomNicknameGenerator;

    @Transactional
    public CommonResult signInBySocial(KakaoProfile kakaoProfile) {

        return signIn(UserSignInRequestDTO.builder()
                .accountId(kakaoProfile.getId())
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());
    }
    @Transactional
    public CommonResult signIn(UserSignInRequestDTO userSignInRequestDTO) {
        Optional<User> user = userRepository.findByAccountIdAndProvider(
                userSignInRequestDTO.accountId(),
                userSignInRequestDTO.provider()
        );

        if (user.isPresent()) {

            return ResponseFactory.getSingleResult(jwtProvider.createTokenDTO(
                    user.get().getUserId(), user.get().getRole())
            );
        }

        User savedUser = userRepository.save(userSignInRequestDTO.toEntity());

        return ResponseFactory.getSingleResult(jwtProvider.createTokenDTO(savedUser.getUserId(), savedUser.getRole()));
    }

    @Transactional
    public UserLoginResponseDTO signIn(GoogleUserInfoDTO userInfo) {
        String accountId = userInfo.id();

        Optional<User> optionalUser = userRepository.findByAccountId(accountId);

        User user = optionalUser.orElseGet(() -> {
            try {
                return signUp(userInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        TokenResponseDTO tokenDTO = jwtProvider.createTokenDTO(user.getUserId(), user.getRole());

        UserLoginResponseDTO response = UserLoginResponseDTO.of(user, tokenDTO);

        return response;
    }

    @Transactional
    public User signUp(GoogleUserInfoDTO googleUserInfoDTO) throws JsonProcessingException {
        String randomNickname = randomNicknameGenerator.generateRandomNickname();
        User user = googleUserInfoDTO.toEntity(randomNickname);
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
