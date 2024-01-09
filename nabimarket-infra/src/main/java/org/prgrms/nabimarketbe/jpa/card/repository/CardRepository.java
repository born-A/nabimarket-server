package org.prgrms.nabimarketbe.jpa.card.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.jpa.card.entity.Card;
import org.prgrms.nabimarketbe.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    @Query("SELECT c FROM Card c WHERE c.cardId = :cardId AND c.user = :user AND c.isActive = true")
    Optional<Card> findByCardIdAndUser(@Param("cardId") Long cardId, @Param("user") User user);

    @Query("SELECT c FROM Card c WHERE c.cardId = :cardId AND c.isActive = true")
    Optional<Card> findActiveCardById(@Param("cardId") Long cardId);

    @Query("SELECT c FROM Card c WHERE c.cardId = :cardId")
    Optional<Card> findExistingCardById(@Param("cardId") Long cardId);

    @Query("SELECT c.viewCount FROM Card c WHERE c.cardId = :cardId")
    Integer getCardViewCountById(@Param("cardId") Long cardId);

    @Query("UPDATE Card c SET c.viewCount = :viewCount WHERE c.cardId = :cardId")
    @Modifying(clearAutomatically = true)
    void updateViewCountByCardId(@Param("cardId") Long cardId, @Param("viewCount") Integer viewCount);
}
