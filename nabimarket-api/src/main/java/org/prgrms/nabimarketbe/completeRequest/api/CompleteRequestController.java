package org.prgrms.nabimarketbe.completeRequest.api;

import org.prgrms.nabimarketbe.completeRequest.dto.request.CompleteRequestDTO;
import org.prgrms.nabimarketbe.completeRequest.dto.request.CompleteRequestUpdateDTO;
import org.prgrms.nabimarketbe.completeRequest.dto.response.CompleteRequestResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.service.CompleteRequestService;
import org.prgrms.nabimarketbe.completeRequest.wrapper.CompleteRequestInfoDTO;
import org.prgrms.nabimarketbe.completeRequest.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.model.ResponseFactory;
import org.prgrms.nabimarketbe.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "거래 성사", description = "거래 성사 요청 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/complete-requests")
public class CompleteRequestController {
    private final CompleteRequestService completeRequestService;

    @Operation(summary = "거래 성사 요청", description = "거래 성사 요청을 보냅니다.")
    @PostMapping
    public ResponseEntity<SingleResult<CompleteRequestResponseDTO>> createCompleteRequest(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "거래 성사 요청값", required = true)
        @RequestBody CompleteRequestDTO completeRequestDTO
    ) {
        CompleteRequestResponseDTO completeRequestResponseDTO = completeRequestService.createCompleteRequest(
            token,
            completeRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(completeRequestResponseDTO));
    }

    @Operation(summary = "거래 성사 히스토리", description = "성사된 거래의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<SingleResult<HistoryListReadLimitResponseDTO>> getHistoryBySize(
        @Parameter(description = "페이지 size", required = true)
        @RequestParam Integer size
    ) {
        HistoryListReadLimitResponseDTO historyListReadLimitResponseDTO = completeRequestService.getHistoryBySize(size);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(historyListReadLimitResponseDTO));
    }

    @Operation(summary = "유저별 거래 성사 내역 조회", description = "유저 별 성사된 거래의 목록을 조회합니다.")
    @GetMapping("/user")
    public ResponseEntity<SingleResult<HistoryListReadPagingResponseDTO>> getHistoryByUser(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "커서 id")
        @RequestParam(required = false) String cursorId,
        @Parameter(description = "페이지 size", required = true)
        @RequestParam Integer size
    ) {
        HistoryListReadPagingResponseDTO historyByUser = completeRequestService.getHistoryByUser(
            token,
            cursorId,
            size
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(historyByUser));
    }

    @Operation(summary = "거래 성사 요청 단건 조회", description = "거래 성사 요청 단건 조회합니다.")
    @GetMapping("/{completeRequestId}")
    public ResponseEntity<SingleResult<CompleteRequestInfoDTO>> getCompleteRequestById(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "거래 성사 요청 id(pk)", required = true)
        @PathVariable Long completeRequestId
    ) {
        CompleteRequestInfoDTO completeRequestById = completeRequestService.getCompleteRequestById(
            token,
            completeRequestId
        );

        return ResponseEntity.ok((ResponseFactory.getSingleResult(completeRequestById)));
    }

    @Operation(summary = "거래 성사 요청 응답", description = "들어온 거래 성사 요청에 대해 응답합니다.")
    @PutMapping("/confirm")
    public ResponseEntity<SingleResult<CompleteRequestResponseDTO>> updateCompleteRequestStatus(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "거래 성사 요청에 대한 응답", required = true)
        @RequestBody CompleteRequestUpdateDTO completeRequestUpdateDTO
    ) {
        CompleteRequestResponseDTO completeRequestDTO = completeRequestService.updateCompleteRequestStatus(
            token,
            completeRequestUpdateDTO.fromCardId(),
            completeRequestUpdateDTO.toCardId(),
            completeRequestUpdateDTO.isAccepted()
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(completeRequestDTO));
    }
}
