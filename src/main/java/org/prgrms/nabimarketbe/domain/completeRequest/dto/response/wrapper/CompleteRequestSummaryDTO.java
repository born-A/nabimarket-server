package org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper;

import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardUserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequestStatus;

public record CompleteRequestSummaryDTO(
    CardUserSummaryResponseDTO fromCard,
    CardUserSummaryResponseDTO toCard,
    CompleteRequestStatus status
) {
}
