package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardInfoResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionDetailResponseDTO;

import lombok.Getter;

@Getter
public class SuggestionListReadResponseDTO {
    private CardInfoResponseDTO cardInfo;

    private SuggestionDetailResponseDTO suggestionInfo;
}
