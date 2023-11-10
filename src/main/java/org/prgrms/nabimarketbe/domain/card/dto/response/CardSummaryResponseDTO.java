package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

@Getter
@NoArgsConstructor
public class CardSummaryResponseDTO{
    private Long cardId;

    private String itemName;

    private String thumbnail;

    private PriceRange priceRange;

    @Builder
    private CardSummaryResponseDTO(
        Long cardId,
        String itemName,
        String thumbnail,
        PriceRange priceRange
    ){
        this.cardId = cardId;
        this.itemName = itemName;
        this.thumbnail = thumbnail;
        this.priceRange = priceRange;
    }
}
