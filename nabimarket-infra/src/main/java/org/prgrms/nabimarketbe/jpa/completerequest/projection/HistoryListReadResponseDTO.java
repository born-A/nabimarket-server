package org.prgrms.nabimarketbe.jpa.completerequest.projection;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.jpa.card.projection.CardSummaryResponseDTO;

import lombok.Getter;

@Getter
public class HistoryListReadResponseDTO {
    private Long historyId;

    private CardSummaryResponseDTO fromCard;

    private CardSummaryResponseDTO toCard;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
