package org.prgrms.nabimarketbe.domain.completeRequest.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.global.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "complete_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompleteRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.completeRequestStatus = CompleteRequestStatus.WAITING;
    }

    public void acceptCompleteRequest() {
        this.completeRequestStatus = CompleteRequestStatus.ACCEPTED;
    }

    public void refuseCompleteRequest() {
        this.completeRequestStatus = CompleteRequestStatus.REFUSED;
    }
}
