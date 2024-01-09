package org.prgrms.nabimarketbe.completeRequest;

import lombok.Getter;

import org.prgrms.nabimarketbe.BaseDomain;
import org.prgrms.nabimarketbe.card.Card;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;

@Getter
public class CompleteRequest extends BaseDomain {
    private Long completeRequestId;

    private Card fromCard;

    private Card toCard;

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
