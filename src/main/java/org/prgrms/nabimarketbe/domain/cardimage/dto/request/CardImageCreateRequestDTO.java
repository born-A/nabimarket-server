package org.prgrms.nabimarketbe.domain.cardimage.dto.request;

import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.card.entity.Card;

public record CardImageCreateRequestDTO(String url) {
    public CardImage toCardImageEntity(Card card) {
        return CardImage.builder()
            .imageUrl(url)
            .card(card)
            .build();
    }
}
