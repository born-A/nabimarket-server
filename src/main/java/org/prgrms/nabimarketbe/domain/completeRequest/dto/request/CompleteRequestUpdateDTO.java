package org.prgrms.nabimarketbe.domain.completeRequest.dto.request;

import javax.validation.constraints.NotNull;

public record CompleteRequestUpdateDTO (
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long fromCardId,
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long toCardId,
    @NotNull(message = "수락/거절 여부가 Null이면 안됩니다.")
    Boolean isAccepted
){
}
