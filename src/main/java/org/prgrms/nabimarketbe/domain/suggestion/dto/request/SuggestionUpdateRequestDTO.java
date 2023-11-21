package org.prgrms.nabimarketbe.domain.suggestion.dto.request;

public record SuggestionUpdateRequestDTO(
    Long fromCardId,
    Long toCardId,
    Boolean isAccepted
) {
}
