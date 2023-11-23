package org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardUserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequestStatus;

@Builder
public record CompleteRequestSummaryDTO(
    CardUserSummaryResponseDTO fromCard,
    CardUserSummaryResponseDTO toCard,
    CompleteRequestStatus status
) {
}
