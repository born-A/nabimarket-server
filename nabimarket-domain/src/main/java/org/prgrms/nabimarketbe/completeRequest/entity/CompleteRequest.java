package org.prgrms.nabimarketbe.completeRequest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.prgrms.nabimarketbe.BaseEntity;
import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "complete_requests",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "complete_request_unique",
            columnNames = {"from_card", "to_card"}
        )
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompleteRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complete_request_id", nullable = false)
    private Long completeRequestId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card", nullable = false)
    private Card fromCard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card", nullable = false)
    private Card toCard;

    @Enumerated(EnumType.STRING)
    private CompleteRequestStatus completeRequestStatus;

    public CompleteRequest(
        Card fromCard,
        Card toCard
    ) {
        if (fromCard == null || toCard == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.fromCard = fromCard;
        this.toCard = toCard;
        this.completeRequestStatus = CompleteRequestStatus.WAITING;
    }

    public String createCompleteRequestMessage() {
        String message = String.format(
            "%s님이 %s과의 거래성사를 요청했습니다.",
            fromCard.getUser().getNickname(),
            toCard.getItem().getItemName()
        );

        return message;
    }

    public String createCompleteRequestDecisionMessage(boolean isAccepted) {
        String result = isAccepted ? "성사되었습니다" : "성사되지 못했습니다.";
        String message = String.format(
            "%s과 %s의 거래가 %s",
            this.toCard.getItem().getItemName(),
            this.fromCard.getItem().getItemName(),
            result
        );

        return message;
    }

    public void acceptCompleteRequest() {
        this.completeRequestStatus = CompleteRequestStatus.ACCEPTED;
    }

    public void refuseCompleteRequest() {
        this.completeRequestStatus = CompleteRequestStatus.REFUSED;
    }

    public void deleteCompleteRequest() {
        this.completeRequestStatus = CompleteRequestStatus.DELETED;
    }
}
