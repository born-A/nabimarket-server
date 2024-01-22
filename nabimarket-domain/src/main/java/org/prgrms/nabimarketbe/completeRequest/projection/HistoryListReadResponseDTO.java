package org.prgrms.nabimarketbe.completeRequest.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.card.projection.CardSummaryResponseDTO;

import lombok.Getter;

@Getter
public class HistoryListReadResponseDTO {
    private Long historyId;

    private CardSummaryResponseDTO fromCard;

    private CardSummaryResponseDTO toCard;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
