package org.prgrms.nabimarketbe.card.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.CardStatus;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.cardImage.entity.CardImage;
import org.prgrms.nabimarketbe.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import lombok.Builder;

@Builder
public record CardDetailResponseDTO(
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
    CardStatus status,
    TradeType tradeType,
    String tradeArea,
    Integer dibCount,
    Boolean isMyDib,
    List<CardImageSingleReadResponseDTO> images
) {
    public static CardDetailResponseDTO of(
        Card card,
        List<CardImage> cardImages,
        boolean isMyDib
    ) {
        return CardDetailResponseDTO.builder()
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
            .status(card.getStatus())
            .tradeType(card.getTradeType())
            .tradeArea(card.getTradeArea())
            .dibCount(card.getDibCount())
            .isMyDib(isMyDib)
            .images(cardImages.stream().map(
                cardImage -> CardImageSingleReadResponseDTO.from(cardImage.getImageUrl())).toList())
            .build();
    }
}
