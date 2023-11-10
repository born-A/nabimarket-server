package org.prgrms.nabimarketbe.domain.completeRequest.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSummaryResponseDTO;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class HistoryListReadResponseDTO {
    private CardSummaryResponseDTO fromCard;

    private CardSummaryResponseDTO toCard;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
