package org.prgrms.nabimarketbe.domain.card.dto.response.wrapper;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardListReadResponseDTO;

public record CardPagingResponseDTO(
	List<CardListReadResponseDTO> cardList,
	String nextCursorId
) {
}
