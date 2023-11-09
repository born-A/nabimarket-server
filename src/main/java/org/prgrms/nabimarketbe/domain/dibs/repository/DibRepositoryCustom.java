package org.prgrms.nabimarketbe.domain.dibs.repository;

import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibListReadPagingResponseDTO;

public interface DibRepositoryCustom {
	DibListReadPagingResponseDTO getUserDibsByUserId(
		Long userId,
		Long cursorId,
		Integer size
	);
}
