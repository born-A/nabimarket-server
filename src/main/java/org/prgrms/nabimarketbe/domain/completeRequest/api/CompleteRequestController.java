package org.prgrms.nabimarketbe.domain.completeRequest.api;

import org.prgrms.nabimarketbe.domain.completeRequest.dto.request.CompleteRequestDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.request.CompleteRequestUpdateDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.CompleteRequestResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.CompleteRequestInfoDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.service.CompleteRequestService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/complete-requests")
public class CompleteRequestController {
    private final CompleteRequestService completeRequestService;

    @PostMapping
    public ResponseEntity<SingleResult<CompleteRequestResponseDTO>> createCompleteRequest(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody CompleteRequestDTO completeRequestDTO
    ) {
        CompleteRequestResponseDTO completeRequestResponseDTO = completeRequestService.createCompleteRequest(
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

    @GetMapping("/{completeRequestId}")
    public ResponseEntity<SingleResult<CompleteRequestInfoDTO>> getCompleteRequestById(
        @RequestHeader(name = "Authorization") String token,
        @PathVariable Long completeRequestId
    ) {
        CompleteRequestInfoDTO completeRequestById = completeRequestService.getCompleteRequestById(
            token,
            completeRequestId
        );

        return ResponseEntity.ok((ResponseFactory.getSingleResult(completeRequestById)));
    }

    @PutMapping("/confirm")
    public ResponseEntity<SingleResult<CompleteRequestResponseDTO>> updateCompleteRequestStatus(
        @RequestHeader(name = "Authorization") String token,
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
