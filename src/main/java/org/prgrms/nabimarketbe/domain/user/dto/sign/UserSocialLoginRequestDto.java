package org.prgrms.nabimarketbe.domain.user.dto.sign;

import lombok.*;

@Builder
public record UserSocialLoginRequestDto(String accessToken) {
}
