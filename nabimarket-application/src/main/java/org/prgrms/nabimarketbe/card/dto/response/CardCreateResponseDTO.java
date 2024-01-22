package org.prgrms.nabimarketbe.card.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.cardImage.entity.CardImage;
import org.prgrms.nabimarketbe.cardimage.dto.response.CardImageCreateResponseDTO;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;

import lombok.Builder;

@Builder
public record CardCreateResponseDTO(
    Long cardId,
    String cardTitle,
    String thumbnail,
    String itemName,
    PriceRange priceRange,
    TradeType tradeType,
    CategoryEnum category,
    String tradeArea,
    Boolean pokeAvailable,
    String content,
    Integer viewCount,
    Integer dibCount,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    List<CardImageCreateResponseDTO> images
) {
    public static CardCreateResponseDTO of(
        Card card,
        Item item,
        List<CardImage> cardImages
    ) {
        List<CardImageCreateResponseDTO> cardImagesResponses = cardImages.stream()
            .map(cardImage -> CardImageCreateResponseDTO.from(cardImage.getImageUrl()))
            .toList();

        return CardCreateResponseDTO.builder()
            .cardId(card.getCardId())
            .cardTitle(card.getCardTitle())
            .itemName(item.getItemName())
            .thumbnail(card.getThumbnail())
            .priceRange(item.getPriceRange())
            .tradeType(card.getTradeType())
            .category(item.getCategory().getCategoryName())
            .tradeArea(card.getTradeArea())
            .pokeAvailable(card.getPokeAvailable())
            .content(card.getContent())
            .viewCount(card.getViewCount())
            .dibCount(card.getDibCount())
            .createdAt(card.getCreatedDate())
            .modifiedAt(card.getModifiedDate())
            .images(cardImagesResponses)
            .build();
    }
}
