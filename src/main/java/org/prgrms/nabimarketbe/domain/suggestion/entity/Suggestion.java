package org.prgrms.nabimarketbe.domain.suggestion.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "suggestions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "suggestion_unique",
            columnNames = {"from_card", "to_card"}
        )
    })
public class Suggestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long suggestionId;

    @Enumerated(EnumType.STRING)
    private SuggestionType suggestionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card", nullable = false)
    private Card fromCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card", nullable = false)
    private Card toCard;

    @Enumerated(EnumType.STRING)
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
