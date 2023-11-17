package org.prgrms.nabimarketbe.domain.completeRequest.repository;

import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;

public interface CompleteRequestRepositryCustom {
    HistoryListReadLimitResponseDTO getHistoryBySize(Integer size);

    HistoryListReadPagingResponseDTO getHistoryByUser(User user, String cursorId, Integer size);
}
