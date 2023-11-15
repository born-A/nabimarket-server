package org.prgrms.nabimarketbe.domain.card.repository;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import java.util.List;

public interface CardRepositoryCustom {
    CardListReadPagingResponseDTO getCardsByCondition(
            CategoryEnum category,
            PriceRange priceRange,
            List<CardStatus> status,
            String title,
            String cursorId,
            Integer size
    );

    CardListReadPagingResponseDTO getMyCardsByStatus(
            User user,
            CardStatus status,
            String cursorId,
            Integer size
    );

    List<SuggestionAvailableCardResponseDTO> getSuggestionAvailableCards(
            Long userId,
            Long targetCardId
    );
}
