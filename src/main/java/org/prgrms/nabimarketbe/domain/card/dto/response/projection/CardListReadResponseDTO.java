package org.prgrms.nabimarketbe.domain.card.dto.response.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

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
