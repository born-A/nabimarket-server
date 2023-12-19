package org.prgrms.nabimarketbe.domain.oauth2.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOAuth2TokenDTO(
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
