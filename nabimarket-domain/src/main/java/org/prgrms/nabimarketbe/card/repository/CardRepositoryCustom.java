package org.prgrms.nabimarketbe.card.repository;

import java.util.List;

import org.prgrms.nabimarketbe.card.projection.CardFamousResponseDTO;
import org.prgrms.nabimarketbe.card.projection.CardPagingResponseDTO;
import org.prgrms.nabimarketbe.card.projection.CardSuggestionResponseDTO;
import org.prgrms.nabimarketbe.card.entity.CardStatus;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {
    CardPagingResponseDTO getCardsByCondition(
        CategoryEnum category,
        PriceRange priceRange,
        List<CardStatus> status,
        String title,
        String cursorId,
        Pageable pageable
    );

    CardPagingResponseDTO getCardsByConditionAndOffset(
        CategoryEnum category,
        PriceRange priceRange,
        List<CardStatus> status,
        String title,
        Pageable pageable
    );

    CardPagingResponseDTO getMyCardsByStatus(
        User user,
        CardStatus status,
        String cursorId,
        Integer size
    );

    List<CardSuggestionResponseDTO> getSuggestionAvailableCards(
        Long userId,
        Long targetCardId
    );

    List<CardFamousResponseDTO> getCardsByPopularity();
}
