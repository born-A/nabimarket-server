package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

import java.time.LocalDateTime;

@Builder
public record SuggestionResponseDTO(
    Long suggestionId,
    SuggestionType suggestionType,
    Long fromCardId,
    Long toCardId,
    SuggestionStatus suggestionStatus,
    LocalDateTime createdAt
) {
    public static SuggestionResponseDTO of(
        Long suggestionId,
        SuggestionType suggestionType,
        Long fromCardId,
        Long toCardId,
        SuggestionStatus suggestionStatus,
        LocalDateTime createdAt
    ) {
        return SuggestionResponseDTO.builder()
            .suggestionId(suggestionId)
            .suggestionType(suggestionType)
            .fromCardId(fromCardId)
            .toCardId(toCardId)
            .suggestionStatus(suggestionStatus)
            .createdAt(createdAt)
            .build();
    }
}
