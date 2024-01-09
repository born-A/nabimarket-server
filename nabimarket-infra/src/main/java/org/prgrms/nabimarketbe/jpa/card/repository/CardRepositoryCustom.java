package org.prgrms.nabimarketbe.jpa.card.repository;

import java.util.List;

import org.prgrms.nabimarketbe.jpa.card.projection.CardFamousResponseDTO;
import org.prgrms.nabimarketbe.jpa.card.projection.CardPagingResponseDTO;
import org.prgrms.nabimarketbe.jpa.card.projection.CardSuggestionResponseDTO;
import org.prgrms.nabimarketbe.jpa.card.entity.CardStatus;
import org.prgrms.nabimarketbe.jpa.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.jpa.item.entity.PriceRange;
import org.prgrms.nabimarketbe.jpa.user.entity.User;
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
