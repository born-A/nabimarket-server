package org.prgrms.nabimarketbe.card.projection;

import org.prgrms.nabimarketbe.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardInfoResponseDTO {
    private Long cardId;

    private String thumbnail;

    private String cardTitle;

    private String itemName;

    private PriceRange priceRange;
}
