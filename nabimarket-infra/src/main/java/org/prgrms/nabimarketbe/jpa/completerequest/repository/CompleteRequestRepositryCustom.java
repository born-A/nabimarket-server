package org.prgrms.nabimarketbe.jpa.completerequest.repository;

import org.prgrms.nabimarketbe.jpa.completerequest.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.jpa.completerequest.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.jpa.user.entity.User;

public interface CompleteRequestRepositryCustom {
    HistoryListReadLimitResponseDTO getHistoryBySize(Integer size);

    HistoryListReadPagingResponseDTO getHistoryByUser(User user, String cursorId, Integer size);
}
