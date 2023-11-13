package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CardDetail(
    Long cardId,
    String cardTitle,
    CategoryEnum category,
    String itemName,
    Boolean pokeAvailable,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    Integer viewCount,
    PriceRange priceRange,
    String content,
    CardStatus cardStatus,
    TradeType tradeType,
    String tradeArea,
    Integer dibsCount,
    Boolean isMyDib,
    List<CardImageSingleReadResponseDTO> images
) {
    public static CardDetail of(
        Card card,
        Item item,
        List<CardImage> cardImages
    ) {
        return CardDetail.builder().build();
    }
}
