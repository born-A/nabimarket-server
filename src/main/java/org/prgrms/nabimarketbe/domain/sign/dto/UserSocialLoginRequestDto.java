package org.prgrms.nabimarketbe.domain.sign.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialLoginRequestDto {
    private String accessToken;
}
