package org.prgrms.nabimarketbe.suggestion.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.suggestion.entity.SuggestionType;

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
