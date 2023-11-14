package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import lombok.Getter;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardReadResponseDTO;

@Getter
public class SuggestionListReadResponseDTO{
        private CardReadResponseDTO cardInfo;

        private SuggestionDetailResponseDTO suggestionInfo;
}
