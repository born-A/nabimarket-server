package org.prgrms.nabimarketbe.domain.completeRequest.dto.response;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequest;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequestStatus;

import lombok.Builder;

@Builder
public record CompleteRequestResponseDTO(
    Long completeRequestId,
    Long fromCardId,
    Long toCardId,
    CompleteRequestStatus completeRequestStatus,
    LocalDateTime createdAt
) {
    public static CompleteRequestResponseDTO from(CompleteRequest completeRequest) {
        return CompleteRequestResponseDTO.builder()
            .completeRequestId(completeRequest.getCompleteRequestId())
            .fromCardId(completeRequest.getFromCard().getCardId())
            .toCardId(completeRequest.getToCard().getCardId())
            .completeRequestStatus(completeRequest.getCompleteRequestStatus())
            .createdAt(completeRequest.getCreatedDate())
            .build();
    }
}
