package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CardCreateResponseDTO(
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
        List<CardImageCreateResponseDTO> images
) {
    public static CardCreateResponseDTO of(
            Long cardId,
            String cardTitle,
            String itemName,
            String thumbNailImage,
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
        return CardCreateResponseDTO.builder()
                .cardId(cardId)
                .cardTitle(cardTitle)
                .itemName(itemName)
                .thumbNailImage(thumbNailImage)
                .priceRange(priceRange)
                .tradeType(tradeType)
                .category(category)
                .tradeArea(tradeArea)
                .pokeAvailable(pokeAvailable)
                .content(content)
                .viewCount(viewCount)
                .dibCount(dibCount)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .images(images)
                .build();
    }
}
