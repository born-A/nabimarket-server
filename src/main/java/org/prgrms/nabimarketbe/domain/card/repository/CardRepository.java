package org.prgrms.nabimarketbe.domain.card.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    @Query("SELECT c FROM Card c WHERE c.cardId = :cardId AND c.user = :user AND c.isActive = true")
    Optional<Card> findByCardIdAndUser(@Param("cardId") Long cardId, @Param("user") User user);

    @Query("SELECT c FROM Card c WHERE c.cardId = :cardId AND c.isActive = true")
    Optional<Card> findById(@Param("cardId") Long cardId);
}
