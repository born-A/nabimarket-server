package org.prgrms.nabimarketbe.domain.card.dto.response;

import java.util.List;

public record CardListReadPagingResponseDTO(
        List<CardListReadResponseDTO> cardList,
        String nextCursorId
) {
}
