package org.prgrms.nabimarketbe.domain.card.dto.response;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuggestionAvailableCardResponseDTO {
    private Long cardId;

    private String thumbnail;

    private String itemName;

    private PriceRange priceRange;

    private SuggestionType suggestionType;

    public void updateSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
