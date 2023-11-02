package org.prgrms.nabimarketbe.domain.user.dto.response;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenDTO;

public record UserLoginResponseDTO(
	UserResponseDTO userInfo,
	TokenDTO token
) {
	public static UserLoginResponseDTO of(User user, TokenDTO tokenDTO) {
		UserResponseDTO userResponseDto = UserResponseDTO.from(user);

		UserLoginResponseDTO response = new UserLoginResponseDTO(userResponseDto, tokenDTO);

		return response;
	}
}
