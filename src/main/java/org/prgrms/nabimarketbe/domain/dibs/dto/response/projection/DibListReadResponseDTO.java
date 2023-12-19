package org.prgrms.nabimarketbe.domain.dibs.dto.response.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class DibListReadResponseDTO {
    private Long dibId;

    private Long cardId;

    private String cardTitle;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;

    private CardStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
