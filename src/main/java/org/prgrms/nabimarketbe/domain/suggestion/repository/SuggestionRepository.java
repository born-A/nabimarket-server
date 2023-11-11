package org.prgrms.nabimarketbe.domain.suggestion.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> , SuggestionRepositoryCustom{
    Optional<Suggestion> findSuggestionByFromCardAndToCard(Card fromCard, Card toCard);
}
