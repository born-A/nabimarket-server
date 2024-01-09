package org.prgrms.nabimarketbe.completeRequest.dto.request;

import javax.validation.constraints.NotNull;

public record CompleteRequestDTO(
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long fromCardId,
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long toCardId
) {
}
