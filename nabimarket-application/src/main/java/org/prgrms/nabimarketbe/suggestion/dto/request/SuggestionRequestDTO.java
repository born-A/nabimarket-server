package org.prgrms.nabimarketbe.suggestion.dto.request;

import javax.validation.constraints.NotNull;

public record SuggestionRequestDTO(
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long fromCardId,
    @NotNull(message = "CardId가 Null이면 안됩니다.")
    Long toCardId
) {
}
