package org.prgrms.nabimarketbe.card.projection;

import java.util.List;

public record CardListResponseDTO<T>(
    List<T> cardList
) {
}
