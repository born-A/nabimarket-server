package org.prgrms.nabimarketbe.card.projection;

import org.prgrms.nabimarketbe.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardSummaryResponseDTO {
    private Long cardId;

    private String itemName;

    private String thumbnail;

    private PriceRange priceRange;
}
