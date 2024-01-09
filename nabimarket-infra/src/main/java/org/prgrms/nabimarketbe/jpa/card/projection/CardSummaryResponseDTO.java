package org.prgrms.nabimarketbe.jpa.card.projection;

import org.prgrms.nabimarketbe.jpa.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardSummaryResponseDTO {
    private Long cardId;

    private String itemName;

    private String thumbnail;

    private PriceRange priceRange;
}
