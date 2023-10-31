package org.prgrms.nabimarketbe.domain.user.dto.sign;

import lombok.Builder;

@Builder
public record UserSocialSignupRequestDto(String accessToken) {
}
