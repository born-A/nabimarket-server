package org.prgrms.nabimarketbe.domain.completeRequest.dto.response.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardSummaryResponseDTO;

import lombok.Getter;

@Getter
public class HistoryListReadResponseDTO {
    private Long historyId;

    private CardSummaryResponseDTO fromCard;

    private CardSummaryResponseDTO toCard;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
