package org.prgrms.nabimarketbe.config.security.jwt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExpireDate;
}
