package org.prgrms.nabimarketbe.domain.card.dto.request;

import org.prgrms.nabimarketbe.domain.card.entity.TradeType;
import org.prgrms.nabimarketbe.domain.cardimage.dto.request.CardImageCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import java.util.List;

public record CardCreateRequestDTO(
        String title,
        String thumbNailImage,
        String name,
        PriceRange priceRange,
        TradeType tradeType,
        CategoryEnum category,
        String tradeArea,
        Boolean pokeAvailable,
        String content,
        List<CardImageCreateRequestDTO> images
) {
}
