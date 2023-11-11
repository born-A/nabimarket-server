package org.prgrms.nabimarketbe.domain.completeRequest.dto.response;

import java.util.List;

public record HistoryListReadLimitResponseDTO(
    List<HistoryListReadResponseDTO> historyList
) {
}
