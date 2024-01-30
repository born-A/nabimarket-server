package setup.jwt;

import org.prgrms.nabimarketbe.jwt.dto.TokenResponseDTO;

public class TokenResponseDTOBuilder {
    public static TokenResponseDTO createTestDTO() {
        return TokenResponseDTO.builder()
            .grantType("Bearer")
            .accessToken("testAccessToken")
            .refreshToken("testRefreshToken")
            .accessTokenExpireDate(60 * 60 * 1000L)
            .build();
    }
}
