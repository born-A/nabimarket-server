package org.prgrms.nabimarketbe.domain.suggestion.repository;

import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

public interface SuggestionRepositoryCustom {
    SuggestionListReadPagingResponseDTO getSuggestionsByType(
            DirectionType directionType,
            SuggestionType suggestionType,
            Long cardId,
            String cursorId,
            Integer size
    );
}
