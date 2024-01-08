package org.prgrms.nabimarketbe.jpa.card.projection;

import org.prgrms.nabimarketbe.jpa.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardFamousResponseDTO {
    private Long cardId;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;
}
