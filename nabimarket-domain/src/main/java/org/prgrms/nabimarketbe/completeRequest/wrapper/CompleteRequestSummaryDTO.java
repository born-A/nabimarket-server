package org.prgrms.nabimarketbe.completeRequest.wrapper;

import org.prgrms.nabimarketbe.card.projection.CardUserSummaryResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.entity.CompleteRequestStatus;

import lombok.Builder;

@Builder
public record CompleteRequestSummaryDTO(
    CardUserSummaryResponseDTO fromCard,
    CardUserSummaryResponseDTO toCard,
    CompleteRequestStatus status
) {
}
