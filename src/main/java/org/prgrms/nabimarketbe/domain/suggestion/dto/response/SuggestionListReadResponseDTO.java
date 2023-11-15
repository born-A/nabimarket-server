package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardInfo;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionDetailResponseDTO;

import lombok.Getter;

@Getter
public class SuggestionListReadResponseDTO{
        private CardInfo cardInfo;

        private SuggestionDetailResponseDTO suggestionInfo;
}
