package org.prgrms.nabimarketbe.domain.user.service;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.dto.request.SocialUserInfoDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
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
    public UserLoginResponseDTO signIn(SocialUserInfoDTO socialUserInfoDTO) {
        String accountId = socialUserInfoDTO.accountId();

        Optional<User> optionalUser = userRepository.findByAccountId(accountId);

        User user = optionalUser.orElseGet(() -> {
            try {
                return signUp(socialUserInfoDTO);
            } catch (JsonProcessingException e) {
                throw new BaseException(ErrorCode.EXTERNAL_SERVER_ERROR);
            }
        });

        TokenResponseDTO tokenResponseDTO = jwtProvider.createTokenDTO(user.getUserId(), user.getRole());

        UserLoginResponseDTO response = UserLoginResponseDTO.of(user, tokenResponseDTO);

        return response;
    }

    private User signUp(SocialUserInfoDTO socialUserInfoDTO) throws JsonProcessingException {
        String randomNickname = randomNicknameGenerator.generateRandomNickname();
        User user = socialUserInfoDTO.toEntity(randomNickname);
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
