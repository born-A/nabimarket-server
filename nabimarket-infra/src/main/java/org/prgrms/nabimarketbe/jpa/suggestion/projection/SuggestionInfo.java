package org.prgrms.nabimarketbe.jpa.suggestion.projection;

import org.prgrms.nabimarketbe.jpa.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.jpa.suggestion.entity.SuggestionType;

import lombok.Getter;

@Getter
public class SuggestionInfo {
    private SuggestionType suggestionType;

    private SuggestionStatus suggestionStatus;

    public void updateSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
