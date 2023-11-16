package org.prgrms.nabimarketbe.domain.dibs.repository;

import org.prgrms.nabimarketbe.domain.dibs.dto.response.wrapper.DibListReadPagingResponseDTO;

public interface DibRepositoryCustom {
	DibListReadPagingResponseDTO getUserDibsByDibId(
		Long userId,
		Long cursorId,
		Integer size
	);
}
