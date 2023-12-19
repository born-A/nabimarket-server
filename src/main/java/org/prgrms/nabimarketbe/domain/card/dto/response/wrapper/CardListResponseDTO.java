package org.prgrms.nabimarketbe.domain.card.dto.response.wrapper;

import java.util.List;

public record CardListResponseDTO<T>(
	List<T> cardList
) {
}
