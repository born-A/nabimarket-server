package org.prgrms.nabimarketbe.jpa.completerequest.wrapper;

import org.prgrms.nabimarketbe.jpa.card.projection.CardUserSummaryResponseDTO;
import org.prgrms.nabimarketbe.jpa.completerequest.entity.CompleteRequestStatus;

import lombok.Builder;

@Builder
public record CompleteRequestSummaryDTO(
    CardUserSummaryResponseDTO fromCard,
    CardUserSummaryResponseDTO toCard,
    CompleteRequestStatus status
) {
}
