package org.prgrms.nabimarketbe.domain.completeRequest.api;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.CompleteRequestDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.service.CompleteRequestService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/complete-requests")
public class CompleteRequestController {
    private final CompleteRequestService completeRequestService;

    @PostMapping
    public ResponseEntity<SingleResult<CompleteRequestDTO>> createCompleteRequest(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody org.prgrms.nabimarketbe.domain.completeRequest.dto.request.CompleteRequestDTO completeRequestDTO
    ) {
        CompleteRequestDTO completeRequestResponseDTO = completeRequestService.createCompleteRequest(
            token,
            completeRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(completeRequestResponseDTO));
    }

    @GetMapping
    public ResponseEntity<SingleResult<HistoryListReadLimitResponseDTO>> getHistoryBySize(@RequestParam Integer size) {
        HistoryListReadLimitResponseDTO historyListReadLimitResponseDTO = completeRequestService.getHistoryBySize(size);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(historyListReadLimitResponseDTO));
    }

    @GetMapping("/user")
    public ResponseEntity<SingleResult<HistoryListReadPagingResponseDTO>> getHistoryByUser(
        @RequestHeader("Authorization") String token,
        @RequestParam(required = false) String cursorId,
        @RequestParam Integer size
    ){
        HistoryListReadPagingResponseDTO historyByUser = completeRequestService.getHistoryByUser(
            token,
            cursorId,
            size
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(historyByUser));
    }

    @PutMapping("/confirm")
    public ResponseEntity<SingleResult<CompleteRequestDTO>> updateCompleteRequestStatus(
        @RequestHeader(name = "Authorization") String token,
        @RequestParam Long fromCardId,
        @RequestParam Long toCardId,
        @RequestParam Boolean isAccepted
    ) {
        CompleteRequestDTO completeRequestDTO = completeRequestService.updateCompleteRequestStatus(
            token,
            fromCardId,
            toCardId,
            isAccepted
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(completeRequestDTO));
    }
}
