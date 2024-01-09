package org.prgrms.nabimarketbe.user.dto.response;

public record UserResponseDTO<T>(
    T userInfo
) {
}
