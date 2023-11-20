package org.prgrms.nabimarketbe.domain.card.repository;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardSuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.user.entity.User;
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
}
