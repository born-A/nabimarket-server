package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Getter;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

@Getter
public class CardSummaryResponseDTO{
    private Long cardId;

    private String itemName;

    private String thumbnail;

    private PriceRange priceRange;
}
