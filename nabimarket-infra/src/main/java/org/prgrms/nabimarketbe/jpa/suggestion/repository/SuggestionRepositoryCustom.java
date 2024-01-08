package org.prgrms.nabimarketbe.jpa.suggestion.repository;

import org.prgrms.nabimarketbe.jpa.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.jpa.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.jpa.suggestion.projection.SuggestionListReadPagingResponseDTO;

public interface SuggestionRepositoryCustom {
    SuggestionListReadPagingResponseDTO getSuggestionsByType(
        DirectionType directionType,
        SuggestionType suggestionType,
        Long cardId,
        String cursorId,
        Integer size
    );

}
