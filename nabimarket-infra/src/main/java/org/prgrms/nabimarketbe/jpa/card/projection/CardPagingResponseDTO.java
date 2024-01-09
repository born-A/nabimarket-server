package org.prgrms.nabimarketbe.jpa.card.projection;

import java.util.List;

public record CardPagingResponseDTO(
    List<CardListReadResponseDTO> cardList,
    String nextCursorId
) {
}
