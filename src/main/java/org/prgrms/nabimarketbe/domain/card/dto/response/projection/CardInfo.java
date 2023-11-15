package org.prgrms.nabimarketbe.domain.card.dto.response.projection;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardInfo {
    private Long cardId;

    private String thumbNail;

    private String cardTitle;

    private String itemName;
    
    private PriceRange priceRange;
}
