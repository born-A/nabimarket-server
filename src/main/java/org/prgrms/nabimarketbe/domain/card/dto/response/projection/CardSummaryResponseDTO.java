package org.prgrms.nabimarketbe.domain.card.dto.response.projection;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardSummaryResponseDTO{
    private Long cardId;

    private String itemName;

    private String thumbnail;

    private PriceRange priceRange;
}
