package org.prgrms.nabimarketbe.domain.card.dto.response.projection;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardInfoResponseDTO {
    private Long cardId;

    private String thumbnail;

    private String cardTitle;

    private String itemName;

    private PriceRange priceRange;
}
