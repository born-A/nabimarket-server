package org.prgrms.nabimarketbe.domain.card.dto.response.wrapper;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardInfo;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardSuggestionResponseDTO {
    private CardInfo cardInfo;

    private SuggestionInfo suggestionInfo;
}
