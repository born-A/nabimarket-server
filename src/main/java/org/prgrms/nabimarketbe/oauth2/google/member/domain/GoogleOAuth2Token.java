package org.prgrms.nabimarketbe.oauth2.google.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuth2Token(	// 이건 뭘까

	@JsonProperty("access_token")
	String accessToken,

	@JsonProperty("expires_in")
	int expiresIn,

	String scope,

	@JsonProperty("token_type")
	String tokenType,

	@JsonProperty("id_token")
	String idToken
) {
}
