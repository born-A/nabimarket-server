package org.prgrms.nabimarketbe.oauth2.google.member.domain;

public record LoginResponseDTO(	// 구글 토큰 반환용
	String jwtToken,
	int userNum,
	String accessToken,
	String tokenType
) {
}
