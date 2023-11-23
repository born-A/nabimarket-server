package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.entity.Card;

@Builder
public record CardCondenseResponseDTO(
    Long cardId,

    String itemName,

    String thumbnail
) {
    public static CardCondenseResponseDTO from(Card card) {
        return CardCondenseResponseDTO.builder()
            .cardId(card.getCardId())
            .itemName(card.getItem().getItemName())
            .thumbnail(card.getThumbnail())
            .build();
    }
}
