package org.prgrms.nabimarketbe.jpa.completerequest.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.jpa.completerequest.projection.HistoryListReadResponseDTO;

public record HistoryListReadLimitResponseDTO(
    List<HistoryListReadResponseDTO> historyList
) {
}
