package org.prgrms.nabimarketbe.jpa.card.projection;

import java.util.List;

public record CardListResponseDTO<T>(
    List<T> cardList
) {
}
