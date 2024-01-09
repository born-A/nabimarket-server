package org.prgrms.nabimarketbe.jpa.dibs.repository;

import org.prgrms.nabimarketbe.jpa.dibs.projection.DibListReadPagingResponseDTO;

public interface DibRepositoryCustom {
    DibListReadPagingResponseDTO getUserDibsByDibId(
        Long userId,
        Long cursorId,
        Integer size
    );
}
