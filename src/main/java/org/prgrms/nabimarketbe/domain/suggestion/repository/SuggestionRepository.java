package org.prgrms.nabimarketbe.domain.suggestion.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long>, SuggestionRepositoryCustom {
    @Query("select s "
        + "from Suggestion s "
        + "where s.fromCard = :fromCard and s.toCard = :toCard "
        + "or s.fromCard = :toCard and s.toCard = :fromCard")
    Optional<Suggestion> findSuggestionByFromCardAndToCard(Card fromCard, Card toCard);
}
