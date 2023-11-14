package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuggestionAvailableCardResponseDTO {
    private CardInfo cardInfo;

    private SuggestionInfo suggestionInfo;
}
