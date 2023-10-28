package org.prgrms.nabimarketbe.member.domain;

public record GetSocialOAuth2Res(
	String jwtToken,
	int userNum,
	String accessToken,
	String tokenType
) {
}
