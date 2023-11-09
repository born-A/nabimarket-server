package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
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
        Suggestion suggestion
    ) {
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
