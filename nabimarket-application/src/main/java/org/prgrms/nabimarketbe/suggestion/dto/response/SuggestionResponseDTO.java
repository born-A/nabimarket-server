package org.prgrms.nabimarketbe.suggestion.dto.response;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.jpa.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.jpa.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.jpa.suggestion.entity.SuggestionType;

import lombok.Builder;

@Builder
public record SuggestionResponseDTO(
    Long suggestionId,
    SuggestionType suggestionType,
    Long fromCardId,
    Long toCardId,
    SuggestionStatus suggestionStatus,
    LocalDateTime createdAt
) {
    public static SuggestionResponseDTO from(Suggestion suggestion) {
        return SuggestionResponseDTO.builder()
            .suggestionId(suggestion.getSuggestionId())
            .suggestionType(suggestion.getSuggestionType())
            .fromCardId(suggestion.getFromCard().getCardId())
            .toCardId(suggestion.getToCard().getCardId())
            .suggestionStatus(suggestion.getSuggestionStatus())
            .createdAt(suggestion.getCreatedDate())
            .build();
    }
}
