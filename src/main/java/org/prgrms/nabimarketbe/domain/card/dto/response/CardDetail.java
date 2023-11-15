package org.prgrms.nabimarketbe.domain.card.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Builder;

@Builder
public record CardDetail(
    Long cardId,
    String cardTitle,
    String thumbnail,
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
    Integer dibCount,
    Boolean isMyDib,
    List<CardImageSingleReadResponseDTO> images
) {
    public static CardDetail of(
        Card card,
        List<CardImage> cardImages,
        boolean isMyDib
    ) {
        return CardDetail.builder()
            .cardId(card.getCardId())
            .cardTitle(card.getCardTitle())
            .thumbnail(card.getThumbnail())
            .category(card.getItem().getCategory().getCategoryName())
            .itemName(card.getItem().getItemName())
            .pokeAvailable(card.getPokeAvailable())
            .createdAt(card.getCreatedDate())
            .modifiedAt(card.getModifiedDate())
            .viewCount(card.getViewCount())
            .priceRange(card.getItem().getPriceRange())
            .content(card.getContent())
            .cardStatus(card.getStatus())
            .tradeType(card.getTradeType())
            .tradeArea(card.getTradeArea())
            .dibCount(card.getDibCount())
            .isMyDib(isMyDib)
            .images(cardImages.stream().map(
                cardImage -> CardImageSingleReadResponseDTO.from(cardImage.getImageUrl())).toList())
            .build();
    }
}
