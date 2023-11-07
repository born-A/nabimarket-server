package org.prgrms.nabimarketbe.domain.user.dto.response;

import com.nimbusds.oauth2.sdk.TokenResponse;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;

public record UserLoginResponseDTO(
		UserResponseDTO userInfo,
		TokenResponseDTO token
) {
	public static UserLoginResponseDTO of(User user, TokenResponseDTO tokenDTO) {
		UserResponseDTO userResponseDto = UserResponseDTO.from(user);

		UserLoginResponseDTO response = new UserLoginResponseDTO(userResponseDto, tokenDTO);

		return response;
	}
}
