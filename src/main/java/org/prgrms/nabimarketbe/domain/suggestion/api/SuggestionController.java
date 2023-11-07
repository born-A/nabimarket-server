package org.prgrms.nabimarketbe.domain.suggestion.api;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.suggestion.dto.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.SuggestionResponseDTO;
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

    @PostMapping("/{type}")
    public ResponseEntity<SingleResult<SuggestionResponseDTO>> createSuggestion(
            @PathVariable String type,
            @RequestBody SuggestionRequestDTO suggestionRequestDTO
    ) {
        SuggestionResponseDTO suggestionResponseDTO = suggestionService.createSuggestion(type, suggestionRequestDTO);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(suggestionResponseDTO));
    }
}






