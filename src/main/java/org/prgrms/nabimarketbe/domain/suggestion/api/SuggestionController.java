package org.prgrms.nabimarketbe.domain.suggestion.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.suggestion.dto.request.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.request.SuggestionUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.suggestion.service.SuggestionService;
import org.prgrms.nabimarketbe.global.util.KeyGenerator;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래 제안", description = "거래 제안 요청 관련 API 입니다.")
@RequestMapping("/api/v1/suggestions")
@RequiredArgsConstructor
@RestController
public class SuggestionController {
    private final SuggestionService suggestionService;

    @Operation(summary = "거래 제안 신청", description = "원하는 카드에 거래 제안을 신청합니다.")
    @PostMapping("/{suggestionType}")
    public ResponseEntity<SingleResult<SuggestionResponseDTO>> createSuggestion(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "거래 제안 종류", required = true)
        @PathVariable String suggestionType,
        @Parameter(description = "거래 요청에 필요한 카드 정보", required = true)
        @RequestBody SuggestionRequestDTO suggestionRequestDTO
    ) {
        String key = KeyGenerator.generateSuggestionLockKey(
            suggestionRequestDTO.fromCardId(),
            suggestionRequestDTO.toCardId()
        );

        SuggestionResponseDTO suggestionResponseDTO = suggestionService.createSuggestion(
            token,
            key,
            suggestionType,
            suggestionRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(suggestionResponseDTO));
    }

    @Operation(summary = "제안된 거래 목록 조회", description = "특정 카드에 대한 제안 목록을 조회할 수 있습니다.")
    @GetMapping("/{directionType}/{suggestionType}/{cardId}")
    public ResponseEntity<SingleResult<SuggestionListReadPagingResponseDTO>> getSuggestionsByType(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "받은/보낸", required = true)
        @PathVariable String directionType,
        @Parameter(description = "제안 종류", required = true)
        @PathVariable String suggestionType,
        @Parameter(description = "카드 id(pk)", required = true)
        @PathVariable Long cardId,
        @Parameter(description = "커서 id")
        @RequestParam(required = false) String cursorId,
        @Parameter(description = "page 크기", required = true)
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

    @Operation(summary = "제안 받은 거래에 대한 결정", description = "받은 거래 제안에 대해 결정합니다.")
    @PutMapping("/decision")
    public ResponseEntity<SingleResult<SuggestionResponseDTO>> updateSugggestionStatus(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "제안 결정에 필요한 요청값", required = true)
        @RequestBody SuggestionUpdateRequestDTO suggestionUpdateDTO

    ) {
        SuggestionResponseDTO suggestionResponseDTO = suggestionService.updateSuggestionStatus(
            token,
            suggestionUpdateDTO.fromCardId(),
            suggestionUpdateDTO.toCardId(),
            suggestionUpdateDTO.isAccepted()
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(suggestionResponseDTO));
    }
}
