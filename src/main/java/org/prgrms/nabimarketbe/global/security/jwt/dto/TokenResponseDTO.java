package org.prgrms.nabimarketbe.global.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
	private String grantType;

	private String accessToken;

	private String refreshToken;

	private Long accessTokenExpireDate;
}
