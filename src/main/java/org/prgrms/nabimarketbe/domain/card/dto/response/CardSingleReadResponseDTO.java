package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import java.util.List;

@Builder
public record CardSingleReadResponseDTO(
        Long cardId,
        String cardTitle,
        String content,
        String tradeArea,
        Boolean pokeAvailable,
        TradeType tradeType,
        CardStatus status,
        Integer viewCount,
        Integer dibCount,
        String itemName,
        CategoryEnum category,
        PriceRange priceRange,
        List<CardImageSingleReadResponseDTO> images
) {
    public static CardSingleReadResponseDTO of(
            Long cardId,
            String cardTitle,
            String content,
            String tradeArea,
            Boolean pokeAvailable,
            TradeType tradeType,
            CardStatus status,
            Integer viewCount,
            Integer dibCount,
            String itemName,
            CategoryEnum category,
            PriceRange priceRange,
            List<CardImageSingleReadResponseDTO> images
    ) {
        return CardSingleReadResponseDTO.builder()
                .cardId(cardId)
                .cardTitle(cardTitle)
                .content(content)
                .tradeArea(tradeArea)
                .pokeAvailable(pokeAvailable)
                .tradeType(tradeType)
                .status(status)
                .viewCount(viewCount)
                .dibCount(dibCount)
                .itemName(itemName)
                .category(category)
                .priceRange(priceRange)
                .images(images)
                .build();
    }
}
