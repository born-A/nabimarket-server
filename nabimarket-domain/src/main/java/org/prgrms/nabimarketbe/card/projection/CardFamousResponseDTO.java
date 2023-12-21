package org.prgrms.nabimarketbe.card.projection;

import org.prgrms.nabimarketbe.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardFamousResponseDTO {
    private Long cardId;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;
}
