package org.prgrms.nabimarketbe.domain.card.dto.response.wrapper;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardInfoResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardSuggestionResponseDTO {
    private CardInfoResponseDTO cardInfo;

    private SuggestionInfo suggestionInfo;
}
