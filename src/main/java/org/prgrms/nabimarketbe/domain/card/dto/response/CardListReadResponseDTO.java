package org.prgrms.nabimarketbe.domain.card.dto.response;

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

    public CardListReadResponseDTO(
            Long cardId,
            String cardTitle,
            String itemName,
            PriceRange priceRange,
            String thumbnail,
            CardStatus status,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.cardId = cardId;
        this.cardTitle = cardTitle;
        this.itemName = itemName;
        this.priceRange = priceRange;
        this.thumbnail = thumbnail;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
