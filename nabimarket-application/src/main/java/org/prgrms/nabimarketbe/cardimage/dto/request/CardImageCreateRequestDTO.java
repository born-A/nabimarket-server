package org.prgrms.nabimarketbe.cardimage.dto.request;

import javax.validation.constraints.NotBlank;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.cardImage.entity.CardImage;
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
