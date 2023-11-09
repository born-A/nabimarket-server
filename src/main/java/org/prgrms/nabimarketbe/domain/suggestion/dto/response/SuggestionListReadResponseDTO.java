package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SuggestionListReadResponseDTO{
        private Long suggestionId;

        private Long cardId;

        private String cardTitle;

        private String itemName;

        private PriceRange priceRange;

        private String thumbnail;

        private SuggestionType suggestionType;

        private SuggestionStatus suggestionStatus;

        private LocalDateTime createdAt;

        private DirectionType directionType;
}
