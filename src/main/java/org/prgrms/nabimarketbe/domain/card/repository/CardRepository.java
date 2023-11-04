package org.prgrms.nabimarketbe.domain.card.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    @Modifying
    @Query("update Card c set c.viewCount = c.viewCount + 1 where c.cardId = :id")
    void updateViews(@Param("id") Long id);
}
