package org.prgrms.nabimarketbe.suggestion.projection;

import org.prgrms.nabimarketbe.card.projection.CardInfoResponseDTO;

import lombok.Getter;

@Getter
public class SuggestionListReadResponseDTO {
    private CardInfoResponseDTO cardInfo;

    private SuggestionDetailResponseDTO suggestionInfo;
}
