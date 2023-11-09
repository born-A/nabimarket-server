package org.prgrms.nabimarketbe.domain.suggestion.entity;

import lombok.*;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.global.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "suggestions")
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
        this.suggestionType = suggestionType;
        this.fromCard = fromCard;
        this.toCard = toCard;
        this.suggestionStatus = SuggestionStatus.WAITING;
    }

    public void acceptSuggestion() {
        this.suggestionStatus = SuggestionStatus.ACCEPTED;
    }

    public void refuseSuggestion() {
        this.suggestionStatus = SuggestionStatus.REFUSED;
    }
}
