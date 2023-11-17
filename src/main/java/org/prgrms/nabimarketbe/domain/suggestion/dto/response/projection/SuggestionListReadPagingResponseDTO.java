package org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection;

import java.util.List;

import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionListReadResponseDTO;

public record SuggestionListReadPagingResponseDTO(
        List<SuggestionListReadResponseDTO> suggestionList,
        String nextCursorId
) {
}
