package org.prgrms.nabimarketbe.suggestion.dto.request;

import javax.validation.constraints.NotNull;

public record SuggestionUpdateRequestDTO(
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long fromCardId,
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long toCardId,
    @NotNull(message = "수락여부가 Null이면 안됩니다.")
    Boolean isAccepted
) {
}
