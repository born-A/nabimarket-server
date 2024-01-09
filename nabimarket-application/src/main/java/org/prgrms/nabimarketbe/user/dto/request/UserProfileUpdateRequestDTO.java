package org.prgrms.nabimarketbe.user.dto.request;

import javax.validation.constraints.NotBlank;

public record UserProfileUpdateRequestDTO(
    @NotBlank(message = "이미지가 비어있으면 안됩니다.")
    String imageUrl
) {
}
