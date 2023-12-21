package org.prgrms.nabimarketbe.dibs.repository;

import org.prgrms.nabimarketbe.dibs.wrapper.DibListReadPagingResponseDTO;

public interface DibRepositoryCustom {
    DibListReadPagingResponseDTO getUserDibsByDibId(
        Long userId,
        Long cursorId,
        Integer size
    );
}
