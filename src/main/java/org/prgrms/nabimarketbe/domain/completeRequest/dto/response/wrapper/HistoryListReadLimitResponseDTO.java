package org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.projection.HistoryListReadResponseDTO;

public record HistoryListReadLimitResponseDTO(
    List<HistoryListReadResponseDTO> historyList
) {
}
