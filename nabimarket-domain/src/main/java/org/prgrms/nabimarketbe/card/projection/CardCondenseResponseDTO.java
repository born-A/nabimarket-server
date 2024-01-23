package org.prgrms.nabimarketbe.card.projection;

import org.prgrms.nabimarketbe.card.entity.Card;

import lombok.Builder;

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
