package org.prgrms.nabimarketbe.domain.suggestion.api;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.suggestion.dto.request.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.suggestion.service.SuggestionService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/suggestions")
@RequiredArgsConstructor
@RestController
public class SuggestionController {
    private final SuggestionService suggestionService;

    @PostMapping("/{suggestionType}")
    public ResponseEntity<SingleResult<SuggestionResponseDTO>> createSuggestion(
        @RequestHeader(name = "Authorization") String token,
        @PathVariable String suggestionType,
        @RequestBody SuggestionRequestDTO suggestionRequestDTO
    ) {
        SuggestionResponseDTO suggestionResponseDTO = suggestionService.createSuggestion(
            token,
            suggestionType,
            suggestionRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(suggestionResponseDTO));
    }

    @GetMapping("/{directionType}/{suggestionType}/{cardId}")
    public ResponseEntity<SingleResult<SuggestionListReadPagingResponseDTO>> getSuggestionsByType(
        @RequestHeader(name = "Authorization") String token,
        @PathVariable String directionType,
        @PathVariable String suggestionType,
        @PathVariable Long cardId,
        @RequestParam(required = false) String cursorId,
        @RequestParam Integer size
    ) {
        SuggestionListReadPagingResponseDTO receivedSuggestions = suggestionService.getSuggestionsByType(
            token,
            DirectionType.valueOf(directionType),
            SuggestionType.valueOf(suggestionType),
            cardId,
            cursorId,
            size
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(receivedSuggestions));
    }

    @PutMapping("/decision")
    public ResponseEntity<SingleResult<SuggestionResponseDTO>> updateSugggestionStatus(
        @RequestHeader(name = "Authorization") String token,
        @RequestParam Long fromCardId,
        @RequestParam Long toCardId,
        @RequestParam Boolean isAccepted
    ) {
        SuggestionResponseDTO suggestionResponseDTO = suggestionService.updateSuggestionStatus(
            token,
            fromCardId,
            toCardId,
            isAccepted
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(suggestionResponseDTO));
    }
}






