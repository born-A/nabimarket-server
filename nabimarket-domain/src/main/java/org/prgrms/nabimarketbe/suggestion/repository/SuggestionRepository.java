package org.prgrms.nabimarketbe.suggestion.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long>, SuggestionRepositoryCustom {
    @Query("select s "
        + "from Suggestion s "
        + "where s.fromCard = :fromCard and s.toCard = :toCard "
        + "or s.fromCard = :toCard and s.toCard = :fromCard")
    Optional<Suggestion> findSuggestionByFromCardAndToCard(
        @Param("fromCard") Card fromCard,
        @Param("toCard") Card toCard
    );

    @Query(
        "select count(s.suggestionId) > 0 " +
            "from Suggestion s "
            + "where s.fromCard = :fromCard and s.toCard = :toCard "
            + "or s.fromCard = :toCard and s.toCard = :fromCard")
    Boolean exists(@Param("fromCard") Card fromCard, @Param("toCard") Card toCard);
}
