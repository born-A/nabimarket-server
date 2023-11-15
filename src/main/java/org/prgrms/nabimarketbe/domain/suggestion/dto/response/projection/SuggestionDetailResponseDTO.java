package org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

import lombok.Getter;

@Getter
public class SuggestionDetailResponseDTO {
    private Long suggestionId;
    private SuggestionType suggestionType;
    private SuggestionStatus suggestionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private DirectionType directionType;
}
