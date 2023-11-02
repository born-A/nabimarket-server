package org.prgrms.nabimarketbe.domain.card.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
}
