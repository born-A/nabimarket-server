package org.prgrms.nabimarketbe.user.dto.response;

import org.prgrms.nabimarketbe.jwt.dto.TokenResponseDTO;
import org.prgrms.nabimarketbe.jpa.user.entity.User;

public record UserLoginResponseDTO(
    UserGetResponseDTO userInfo,
    TokenResponseDTO token
) {
    public static UserLoginResponseDTO of(User user, TokenResponseDTO tokenResponseDTO) {
        UserGetResponseDTO userGetResponseDto = UserGetResponseDTO.from(user);

        UserLoginResponseDTO response = new UserLoginResponseDTO(userGetResponseDto, tokenResponseDTO);

        return response;
    }
}
