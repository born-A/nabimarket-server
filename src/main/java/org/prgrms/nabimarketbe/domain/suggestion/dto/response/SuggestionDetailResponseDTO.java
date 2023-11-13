package org.prgrms.nabimarketbe.domain.suggestion.dto.response;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;

import java.time.LocalDateTime;

@Getter
public class SuggestionDetailResponseDTO {
    private Long suggestionId;
    private SuggestionType suggestionType;
    private SuggestionStatus suggestionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private DirectionType directionType;
}
