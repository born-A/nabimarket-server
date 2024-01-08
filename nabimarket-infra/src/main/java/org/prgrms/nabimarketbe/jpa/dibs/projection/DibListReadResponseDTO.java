package org.prgrms.nabimarketbe.jpa.dibs.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.jpa.card.entity.CardStatus;
import org.prgrms.nabimarketbe.jpa.item.entity.PriceRange;

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
