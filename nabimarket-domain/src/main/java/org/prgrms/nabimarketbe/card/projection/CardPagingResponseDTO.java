package org.prgrms.nabimarketbe.card.projection;

import java.util.List;

public record CardPagingResponseDTO(
    List<CardListReadResponseDTO> cardList,
    String nextCursorId
) {
}
