package org.prgrms.nabimarketbe.domain.card.dto.response.projection;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

@Getter
public class CardFamousResponseDTO {
    private Long cardId;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;
}
