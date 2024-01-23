package org.prgrms.nabimarketbe.suggestion.repository;

import org.prgrms.nabimarketbe.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.suggestion.projection.SuggestionListReadPagingResponseDTO;

public interface SuggestionRepositoryCustom {
    SuggestionListReadPagingResponseDTO getSuggestionsByType(
        DirectionType directionType,
        SuggestionType suggestionType,
        Long cardId,
        String cursorId,
        Integer size
    );

}
