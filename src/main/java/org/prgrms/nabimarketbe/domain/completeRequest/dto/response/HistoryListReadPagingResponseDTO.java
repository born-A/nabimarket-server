package org.prgrms.nabimarketbe.domain.completeRequest.dto.response;

import java.util.List;

public record HistoryListReadPagingResponseDTO(
    List<HistoryListReadResponseDTO> historyList,
    String cursorId
) {
}
