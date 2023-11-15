package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

@Getter
public class CardInfo {
    private Long cardId;
    private String thumbNail;
    private String cardTitle;
    private String itemName;
    private PriceRange priceRange;
}