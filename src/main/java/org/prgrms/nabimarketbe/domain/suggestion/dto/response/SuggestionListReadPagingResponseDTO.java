package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import java.util.List;

public record SuggestionListReadPagingResponseDTO(
        List<SuggestionListReadResponseDTO> suggestionList,
        String nextCursorId
) {
}
