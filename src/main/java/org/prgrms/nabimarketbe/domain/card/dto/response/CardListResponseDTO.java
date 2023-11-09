package org.prgrms.nabimarketbe.domain.card.dto.response;

import java.util.List;

public record CardListResponseDTO<T>(
        List<T> cardList
) {
}
