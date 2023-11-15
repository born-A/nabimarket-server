package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

@Getter
public class SuggestionInfo {
    private SuggestionType suggestionType;
    private SuggestionStatus suggestionStatus;

    public void updateSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
