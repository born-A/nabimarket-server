package org.prgrms.nabimarketbe.jpa.completerequest.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.jpa.completerequest.projection.HistoryListReadResponseDTO;

public record HistoryListReadPagingResponseDTO(
    List<HistoryListReadResponseDTO> historyList,
    String nextCursorId
) {
}
