package org.prgrms.nabimarketbe.jpa.suggestion.projection;

import java.util.List;

public record SuggestionListReadPagingResponseDTO(
    List<SuggestionListReadResponseDTO> suggestionList,
    String nextCursorId
) {
}
