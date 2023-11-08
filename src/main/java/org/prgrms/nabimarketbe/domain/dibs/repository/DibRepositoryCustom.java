package org.prgrms.nabimarketbe.domain.dibs.repository;

import java.util.List;

import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibListReadResponseDTO;

public interface DibRepositoryCustom {
	List<DibListReadResponseDTO> getUserDibsByUserId(
		Long userId,
		Long cursorId
	);
}
