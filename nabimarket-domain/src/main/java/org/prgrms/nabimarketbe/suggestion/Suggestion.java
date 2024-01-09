package org.prgrms.nabimarketbe.suggestion;

import lombok.Builder;
import lombok.Getter;

import org.prgrms.nabimarketbe.BaseDomain;
import org.prgrms.nabimarketbe.card.Card;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;

@Getter
public class Suggestion extends BaseDomain {
    private Long suggestionId;

    private SuggestionType suggestionType;

    private Card fromCard;

    private Card toCard;

    private SuggestionStatus suggestionStatus;

    @Builder
    public Suggestion(
        SuggestionType suggestionType,
        Card fromCard,
        Card toCard
    ) {
        if (suggestionType == null || fromCard == null || toCard == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.suggestionType = suggestionType;
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.suggestionStatus = SuggestionStatus.WAITING;
    }

    public boolean isAccepted() {
        return suggestionStatus.equals(SuggestionStatus.ACCEPTED);
    }

    public void decideSuggestion(Boolean isAccpeted) {
        if (isAccpeted) {
            acceptSuggestion();
        } else {
            refuseSuggestion();
        }
    }

    public String createSuggestionRequestMessage() {
        String message = String.format(
            "%s에 대한 %s님의 새로운 제안이 도착하였습니다.",
            toCard.getItem().getItemName(),
            fromCard.getUser().getNickname()
        );

        return message;
    }

    public String createSuggestionDecisionMessage(boolean isAccepted) {
        String suggestionResult = isAccepted ? "수락" : "거절";
        String suggestionType = this.suggestionType.getName();
        String message = String.format(
            "%s에 대한 %s의 %s이(가) %s 되었습니다.",
            toCard.getItem().getItemName(),
            fromCard.getItem().getItemName(),
            suggestionType,
            suggestionResult
        );

        return message;
    }

    private void acceptSuggestion() {
        this.suggestionStatus = SuggestionStatus.ACCEPTED;
    }

    private void refuseSuggestion() {
        this.suggestionStatus = SuggestionStatus.REFUSED;
    }
}
