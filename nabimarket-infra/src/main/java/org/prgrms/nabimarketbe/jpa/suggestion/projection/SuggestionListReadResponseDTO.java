package org.prgrms.nabimarketbe.jpa.suggestion.projection;

import org.prgrms.nabimarketbe.jpa.card.projection.CardInfoResponseDTO;

import lombok.Getter;

@Getter
public class SuggestionListReadResponseDTO {
    private CardInfoResponseDTO cardInfo;

    private SuggestionDetailResponseDTO suggestionInfo;
}
