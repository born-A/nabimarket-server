package org.prgrms.nabimarketbe.card.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.card.entity.CardStatus;
import org.prgrms.nabimarketbe.item.entity.PriceRange;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CardListReadResponseDTO {
    private Long cardId;

    private String cardTitle;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;

    private CardStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
