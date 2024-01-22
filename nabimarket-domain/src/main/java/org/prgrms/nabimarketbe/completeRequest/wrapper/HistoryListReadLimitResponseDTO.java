package org.prgrms.nabimarketbe.completeRequest.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.completeRequest.projection.HistoryListReadResponseDTO;

public record HistoryListReadLimitResponseDTO(
    List<HistoryListReadResponseDTO> historyList
) {
}
