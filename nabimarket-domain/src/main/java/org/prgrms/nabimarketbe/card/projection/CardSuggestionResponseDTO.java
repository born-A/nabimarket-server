package org.prgrms.nabimarketbe.card.projection;

import org.prgrms.nabimarketbe.suggestion.projection.SuggestionInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardSuggestionResponseDTO {
    private CardInfoResponseDTO cardInfo;

    private SuggestionInfo suggestionInfo;
}
