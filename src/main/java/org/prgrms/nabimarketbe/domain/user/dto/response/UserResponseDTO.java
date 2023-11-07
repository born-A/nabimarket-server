package org.prgrms.nabimarketbe.domain.user.dto.response;

public record UserResponseDTO<T> (
	T userInfo
) {
}
