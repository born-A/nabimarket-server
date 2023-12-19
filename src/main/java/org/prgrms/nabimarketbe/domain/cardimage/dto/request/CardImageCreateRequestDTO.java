package org.prgrms.nabimarketbe.domain.cardimage.dto.request;

import javax.validation.constraints.NotBlank;

import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.card.entity.Card;

public record CardImageCreateRequestDTO(
    @NotBlank
    String url
) {
    public CardImage toCardImageEntity(Card card) {
        return CardImage.builder()
            .imageUrl(url)
            .card(card)
            .build();
    }
}
