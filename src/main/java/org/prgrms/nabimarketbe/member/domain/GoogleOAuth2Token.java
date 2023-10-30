package org.prgrms.nabimarketbe.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuth2Token(

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
