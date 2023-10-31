package org.prgrms.nabimarketbe.oauth2.google.service;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.user.dto.UserLoginResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.UserResponseDto;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.global.security.entity.RefreshToken;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.global.security.jwt.provider.JwtProvider;
import org.prgrms.nabimarketbe.oauth2.google.domain.OAuth2;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleOAuth2Token;
import org.prgrms.nabimarketbe.oauth2.google.dto.GoogleUser;
import org.prgrms.nabimarketbe.oauth2.kakao.repository.RefreshTokenJpaRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service {

	private final OAuth2 oAuth2;

	private final JwtProvider jwtProvider;

	private final UserRepository userRepository;

	private final RefreshTokenJpaRepo tokenJpaRepo;

	public String requestRedirectUrl() {
		String redirectUrl = oAuth2.getOAuth2RedirectUrl();

		return redirectUrl;
	}

	public UserLoginResponseDTO OAuth2Login(String code) throws JsonProcessingException {
		ResponseEntity<String> accessToken = oAuth2.requestAccessToken(code);
		GoogleOAuth2Token googleOAuth2Token = oAuth2.getAccessToken(accessToken);

		ResponseEntity<String> userInfo = oAuth2.requestUserInfo(googleOAuth2Token);

		GoogleUser googleUser = oAuth2.parseUserInfo(userInfo);

		String nameAttributeKey = googleUser.id();

		Optional<User> optionalUser = userRepository.findByNameAttributeKey(nameAttributeKey);ㅋ
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			UserResponseDto userResponseDto = UserResponseDto.from(user);
			TokenResponseDTO token = jwtProvider.createTokenDto(user.getUserId(), user.getRoles());

			UserLoginResponseDTO response = new UserLoginResponseDTO(userResponseDto, token);
			return response;
		}

		UserResponseDto userResponseDto = signUpUser(googleUser);
		TokenResponseDTO token = jwtProvider.createTokenDto(userResponseDto.getUserId(), userResponseDto.getRoles());

		RefreshToken refreshToken = new RefreshToken(userResponseDto.getUserId(), token.getRefreshToken());
		tokenJpaRepo.save(refreshToken);

		UserLoginResponseDTO response = new UserLoginResponseDTO(userResponseDto, token);
		return response;
	}

	private UserResponseDto signUpUser(GoogleUser googleUser) {
		// TODO : 닉네임 자동생성
		User user = googleUser.toEntity("tmpNickName");
		User savedUser = userRepository.save(user);

		return UserResponseDto.from(savedUser);
	}

}
