package org.prgrms.nabimarketbe.global.security.jwt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String grantType;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpireDate;
}
