package org.prgrms.nabimarketbe.domain.user.dto;

import org.prgrms.nabimarketbe.global.security.jwt.dto.TokenResponseDTO;

public record UserLoginResponseDTO(
	UserResponseDto user,
	TokenResponseDTO token
) {
}
