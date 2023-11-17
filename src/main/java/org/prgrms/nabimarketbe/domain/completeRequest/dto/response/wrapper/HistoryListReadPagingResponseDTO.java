package org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.projection.HistoryListReadResponseDTO;

public record HistoryListReadPagingResponseDTO(
    List<HistoryListReadResponseDTO> historyList,
    String cursorId
) {
}
