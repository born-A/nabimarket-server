package org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection;

import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

import lombok.Getter;

@Getter
public class SuggestionInfo {
    private SuggestionType suggestionType;

    private SuggestionStatus suggestionStatus;

    public void updateSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
