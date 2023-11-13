package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CardUpdateResponseDTO(
    Long cardId,
    String cardTitle,
    String thumbNailImage,
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
    List<CardImageUpdateResponseDTO> images
) {
    public static CardUpdateResponseDTO of(
        Card card,
        Item item,
        List<CardImage> cardImages
    ) {
        List<CardImageUpdateResponseDTO> cardImagesResponses = cardImages.stream()
            .map(cardImage -> CardImageUpdateResponseDTO.from(cardImage.getImageUrl()))
            .toList();

        return CardUpdateResponseDTO.builder()
            .cardId(card.getCardId())
            .cardTitle(card.getCardTitle())
            .itemName(item.getItemName())
            .thumbNailImage(card.getThumbNailImage())
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
