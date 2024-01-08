package org.prgrms.nabimarketbe.jpa.card.projection;

import org.prgrms.nabimarketbe.jpa.suggestion.projection.SuggestionInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardSuggestionResponseDTO {
    private CardInfoResponseDTO cardInfo;

    private SuggestionInfo suggestionInfo;
}
