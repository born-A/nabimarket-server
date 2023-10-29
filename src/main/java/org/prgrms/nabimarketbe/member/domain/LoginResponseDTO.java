package org.prgrms.nabimarketbe.member.domain;

public record LoginResponseDTO(
	String jwtToken,
	int userNum,
	String accessToken,
	String tokenType
) {
}
