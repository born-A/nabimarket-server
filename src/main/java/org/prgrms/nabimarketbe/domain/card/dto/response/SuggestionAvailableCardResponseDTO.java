package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

@Getter
@NoArgsConstructor
public class SuggestionAvailableCardResponseDTO {
    private Long cardId;
    private String thumbNail;
    private String itemName;
    private PriceRange priceRange;
    private SuggestionType suggestionType;

    public void setSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
