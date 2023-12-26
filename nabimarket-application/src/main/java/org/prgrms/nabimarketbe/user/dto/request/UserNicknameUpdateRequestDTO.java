package org.prgrms.nabimarketbe.user.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserNicknameUpdateRequestDTO(
    @NotBlank(message = "닉네임은 비어있으면 안됩니다.")
    @Size(max = 20)
    String nickname
) {
}
