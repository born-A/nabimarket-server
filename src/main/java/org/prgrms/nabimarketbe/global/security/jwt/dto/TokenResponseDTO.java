package org.prgrms.nabimarketbe.global.security.jwt.dto;

import lombok.*;

@Builder
public record TokenResponseDTO(
    String grantType,
    String accessToken,
    String refreshToken,
    Long accessTokenExpireDate
) {
}
