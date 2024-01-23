package org.prgrms.nabimarketbe.completeRequest.repository;

import org.prgrms.nabimarketbe.completeRequest.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.user.entity.User;

public interface CompleteRequestRepositryCustom {
    HistoryListReadLimitResponseDTO getHistoryBySize(Integer size);

    HistoryListReadPagingResponseDTO getHistoryByUser(User user, String cursorId, Integer size);
}
