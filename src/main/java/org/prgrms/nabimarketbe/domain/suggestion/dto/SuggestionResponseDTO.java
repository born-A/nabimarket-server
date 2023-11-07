package org.prgrms.nabimarketbe.domain.suggestion.dto;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

@Builder
public record SuggestionResponseDTO(
        SuggestionType suggestionType,
        Long fromCardId,
        Long toCardId,
        SuggestionStatus suggestionStatus
) {
    public static SuggestionResponseDTO of(
            SuggestionType suggestionType,
            Long fromCardId,
            Long toCardId,
            SuggestionStatus suggestionStatus
    ) {
        return SuggestionResponseDTO.builder()
                .suggestionType(suggestionType)
                .fromCardId(fromCardId)
                .toCardId(toCardId)
                .suggestionStatus(suggestionStatus)
                .build();
    }
}
