package org.prgrms.nabimarketbe.domain.security.jwt.dto;

import lombok.Builder;

@Builder
public record TokenRequestDto (
        String accessToken,
        String refreshToken
) {
}
