package org.prgrms.nabimarketbe.suggestion.projection;

import org.prgrms.nabimarketbe.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionType;

import lombok.Getter;

@Getter
public class SuggestionInfo {
    private SuggestionType suggestionType;

    private SuggestionStatus suggestionStatus;

    public void updateSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
