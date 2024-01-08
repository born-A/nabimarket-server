package org.prgrms.nabimarketbe.jpa.card.projection;

import org.prgrms.nabimarketbe.jpa.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardInfoResponseDTO {
    private Long cardId;

    private String thumbnail;

    private String cardTitle;

    private String itemName;

    private PriceRange priceRange;
}
