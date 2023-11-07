package org.prgrms.nabimarketbe.domain.user.dto.response;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;

public record UserLoginResponseDTO(
	UserGetResponseDTO userInfo,
	TokenResponseDTO token
) {
	public static UserLoginResponseDTO of(User user, TokenResponseDTO tokenResponseDTO) {
		UserGetResponseDTO userGetResponseDto = UserGetResponseDTO.from(user);

		UserLoginResponseDTO response = new UserLoginResponseDTO(userGetResponseDto, tokenResponseDTO);

		return response;
	}
}
