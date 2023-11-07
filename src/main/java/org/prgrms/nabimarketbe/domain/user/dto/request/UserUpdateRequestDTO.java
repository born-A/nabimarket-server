package org.prgrms.nabimarketbe.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

public record UserUpdateRequestDTO(
	@NotBlank(message = "닉네임은 비어있으면 안됩니다.")
	String nickname
) {
}
